package com.vidya.flow;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

@Component
public class PollingFlow {

	@Bean
	public IntegrationFlow pollFlow() {
		return IntegrationFlow
				.from(this.integerMessageSource(), c -> c.poller(Pollers.fixedRate(3000).maxMessagesPerPoll(2)))

				.filter((Integer p) -> p >= 0)

				.log(Level.INFO, p -> p.getPayload().toString())
				// .transform(Object::toString)
				// .handle(System.out::println)
				.get();
	}

	@Bean
	public MethodInvokingMessageSource integerMessageSource() {
		MethodInvokingMessageSource source = new MethodInvokingMessageSource();
		source.setObject(new AtomicInteger());
		source.setMethodName("getAndIncrement");
		return source;
	}

	// Global default poller
	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata defaultPoller() {
		PollerMetadata pollerMetadata = new PollerMetadata();
		pollerMetadata.setTrigger(new PeriodicTrigger(10));
		return pollerMetadata;
	}
}
