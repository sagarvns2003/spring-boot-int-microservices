package com.vidya.flow;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Filter;
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
				//.log(Level.INFO, p -> p.getPayload().toString())
				.channel("nextChannel")
				// .handle(System.out::println)
				.get();
	}

    @Bean
    public AtomicInteger integerSource() {
        return new AtomicInteger();
    }
    
    
    @Filter(inputChannel = "nextChannel", 
    		    outputChannel = "validChannel",   //true will come here
    		    discardChannel = "invalidChannel") //false will come here
	private boolean validateMessage(Message<?> message) {
		Integer number = (Integer) message.getPayload();
		//Simple check for even number
		if (number % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}
    /*
    @ServiceActivator(inputChannel = "validChannel", requiresReply = "false")
	public void validMessage(Message<?> message) throws ExecutionException, InterruptedException {
		//System.out.println(": "+message.getPayload());
	}*/
    
    @Bean
	public IntegrationFlow validMessageFlow() {
		return IntegrationFlow.from("validChannel")
				.log(Level.INFO, p -> "Valid message " + p.getPayload().toString())
				.handle(message -> {
					//Do message processing here
				})
				.get();
	}
    
    @Bean
	public IntegrationFlow inValidMessageFlow() {
		return IntegrationFlow.from("invalidChannel")
				.log(Level.INFO, p -> "Invalid message " + p.getPayload().toString())
				.handle(message -> {
					//Do message processing here
				})
				.get();
	}
}
