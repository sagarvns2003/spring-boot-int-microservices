package com.vidya.flow;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class PollingFlow {

	@Bean
	public IntegrationFlow pollFlow() {
		return IntegrationFlow
				.fromSupplier(integerSource()::getAndIncrement, c -> c.poller(Pollers.fixedRate(3000).maxMessagesPerPoll(2)))
				.filter((Integer p) -> p >= 0)
				.log(Level.INFO, p -> p.getPayload().toString())
				.transform(Object::toString)
				.channel("nextChannel")
				// .handle(System.out::println)
				.get();
	}

    @Bean
    public AtomicInteger integerSource() {
        return new AtomicInteger();
    }
    
    @ServiceActivator(inputChannel = "nextChannel", requiresReply = "false")
	public void getPerson(Message<?> message) throws ExecutionException, InterruptedException {
		//System.out.println(": "+message.getPayload());
	}
}
