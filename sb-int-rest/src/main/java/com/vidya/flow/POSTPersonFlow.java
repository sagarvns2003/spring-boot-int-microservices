/**
 * 
 */
package com.vidya.flow;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.ConsumerEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.stereotype.Component;

import com.vidya.dao.entity.Person;
import com.vidya.dao.repository.PersonRepository;
import com.vidya.util.JsonUtil;

import jakarta.persistence.EntityManager;

/**
 * @author Vidya
 */
@Component
public class POSTPersonFlow {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	/*
	 * POST
	 * Create person example
	 */
	@Bean
	public HttpRequestHandlingMessagingGateway postPersonInboundHttp() {
		return Http.inboundGateway("/person")
				.requestMapping(r -> r.methods(HttpMethod.POST)
						              .consumes(MediaType.APPLICATION_JSON_VALUE)
						              .produces(MediaType.APPLICATION_JSON_VALUE))
				.requestPayloadType(Person.class) //Convert request body to class type
				.requestChannel("postPersonInboundChannel")
				.replyChannel("replyPostPersonChannel")
				.replyTimeout(20000)
				.getObject();
	}
	
	@Bean
	public IntegrationFlow createPerson() {
		return IntegrationFlow
				.from("postPersonInboundChannel")
				.log(LoggingHandler.Level.INFO, message -> MessageFormat.format("Request to create new person... {0}", JsonUtil.toJsonString(message.getPayload())))
				.handle(Jpa.updatingGateway(entityManager)
						.persistMode(PersistMode.PERSIST)
						.entityClass(Person.class), ConsumerEndpointSpec::transactional)
				.log(LoggingHandler.Level.INFO, message -> "Person created... " + message.getPayload())
				.channel("replyPostPersonChannel")
				.get();
	}
	
	/*
	@Transactional
	@ServiceActivator(inputChannel = "postPersonInboundChannel", outputChannel = "replyPostPersonChannel")
	public Message<?> createPerson(Message<?> message) throws ExecutionException, InterruptedException {
		Person person = (Person) message.getPayload();
		this.personRepository.save(person);
		return MessageBuilder.withPayload(Map.of("status", "success")).build();
	}*/
}