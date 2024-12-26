/** */
package com.vidya.flow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vidya.dao.entity.Person;
import com.vidya.util.CommonUtil;
import com.vidya.util.JsonUtil;
import jakarta.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @author Vidya
 */
@Component
public class POSTTriggerFlow {

  private static final String HEADER_TRIGGER_ID = "TRIGGER_ID";

  private final TypeReference<List<Person>> TYPE_REF_PERSON_LIST = new TypeReference<>() {};

  @Autowired private EntityManager entityManager;

  @Autowired
  @Qualifier("transformPersonTriggerChannel")
  private MessageChannel transformPersonTriggerChannel;

  @Bean
  public HttpRequestHandlingMessagingGateway personTriggerInboundHttp() {
    return Http.inboundGateway("/trigger")
        .requestMapping(
            r ->
                r.methods(HttpMethod.POST)
                    .consumes(MediaType.APPLICATION_JSON_VALUE)
                    .produces(MediaType.APPLICATION_JSON_VALUE))
        .requestChannel("personTriggerInboundChannel")
        .replyChannel("replyTriggerIntegrationChannel")
        .replyTimeout(20000)
        .getObject();
  }

  @Bean
  public IntegrationFlow triggerPersonTransformAndRoute() {
    return IntegrationFlow.from("personTriggerInboundChannel")
        .transform(payload -> JsonUtil.fromJson((byte[]) payload, TYPE_REF_PERSON_LIST))
        .enrichHeaders(h -> h.header(HEADER_TRIGGER_ID, UUID.randomUUID().toString()))
        .log(
            LoggingHandler.Level.INFO,
            message ->
                MessageFormat.format(
                    "Trigger id: {0}, having payload ... {1}",
                    message.getHeaders().get(HEADER_TRIGGER_ID, String.class),
                    message.getPayload()))
        .channel("pubSubChannel") // routing to bridge channel
        .get();
  }

  /*
   * This flow will ack/reply immediately
   */
  @Bean
  public IntegrationFlow ackTrigger() {
    return IntegrationFlow.from(
            "directChannel1") // this channel is connected to bridge pubSubChannel and will have
        // same message
        .transform(
            Message.class,
            message -> {
              String triggerID = message.getHeaders().get(HEADER_TRIGGER_ID, String.class);
              // List<Person> personList = (List<Person>) message.getPayload();
              return Map.of(HEADER_TRIGGER_ID, triggerID, "status", "IN_PROGRESS");
            })
        .channel("replyTriggerIntegrationChannel")
        .get();
  }

  @SuppressWarnings("unchecked")
  @Bean
  public IntegrationFlow triggerPerson() {
    return IntegrationFlow.from(
            "directChannel2") // this channel is connected to bridge pubSubChannel and will have
        // same message
        .handle(
            message -> {
              List<Person> personList = (List<Person>) message.getPayload();
              System.out.println("Total person count: " + personList.size());
              Map<String, Object> allHeaders = CommonUtil.extractAllHeaders(message.getHeaders());
              // Send to another channel for transformation
              this.transformPersonTriggerChannel.send(
                  MessageBuilder.withPayload(personList)
                      .copyHeaders(allHeaders)
                      .setHeader("HEADER_OTHER", "OtherValue")
                      .build());
            })
        .get();
  }

  @SuppressWarnings("unchecked")
  @ServiceActivator(inputChannel = "transformPersonTriggerChannel", requiresReply = "false")
  public void tranformTriggerPerson(Message<?> message)
      throws ExecutionException, InterruptedException {
    String triggerID = message.getHeaders().get(HEADER_TRIGGER_ID, String.class);
    String otherHeader = message.getHeaders().get("HEADER_OTHER", String.class);
    List<Person> personList = (List<Person>) message.getPayload();

    System.out.println("Trigger ID: " + triggerID);
    System.out.println("otherHeader: " + otherHeader);

    if (personList.isEmpty()) {
      throw new ExecutionException("Empty person data for triggerId: " + triggerID, null);
    } else {
      // TODO: do transformation here
      // TODO: Can send to other channel like... otherChannel.send()
      System.out.println("Data... " + JsonUtil.toJsonString(personList));
    }
  }
}
