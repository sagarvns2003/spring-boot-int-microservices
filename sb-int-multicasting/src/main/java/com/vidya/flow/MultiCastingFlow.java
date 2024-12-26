package com.vidya.flow;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class MultiCastingFlow {

  @Autowired
  @Qualifier(value = "pubSubChannel")
  MessageChannel pubSubChannel;

  @Bean
  public IntegrationFlow producerFlow() {
    return IntegrationFlow.fromSupplier(
            this.integerSource()::getAndIncrement,
            c -> c.poller(Pollers.fixedRate(3000).maxMessagesPerPoll(2)))
        .filter((Integer p) -> p >= 0)
        .log(Level.INFO, p -> "From producerFlow: " + p.getPayload().toString())
        .channel(pubSubChannel)
        .get();
  }

  public AtomicInteger integerSource() {
    return new AtomicInteger();
  }

  @Bean
  public IntegrationFlow consumerFlow1() {
    return IntegrationFlow.from("directChannel1")
        .log(Level.INFO, p -> "From consumer of directChannel1: " + p.getPayload().toString())
        .handle(
            message -> {
              // Do nothing as of now
            })
        .get();
  }

  @Bean
  public IntegrationFlow consumerFlow2() {
    return IntegrationFlow.from("directChannel2")
        .log(Level.INFO, p -> "From consumer of directChannel2: " + p.getPayload().toString())
        .handle(
            message -> {
              // Do nothing as of now
            })
        .get();
  }
}
