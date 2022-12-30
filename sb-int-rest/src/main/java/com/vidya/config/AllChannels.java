/**
 * 
 */
package com.vidya.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.MessageChannels;

/**
 * @author Vidya
 */
@Configuration
public class AllChannels {

	private ExecutorService executor = Executors.newCachedThreadPool();

	@Bean(name = "inboundHealthChannel")
	public PublishSubscribeChannel inboundHealthChannel() {
		return MessageChannels.publishSubscribe(executor).get();
	}

	@Bean(name = "replyHealthChannel")
	public DirectChannel replyHealthChannel() {
		return new DirectChannel();
	}

	@Bean(name = "inboundHelloChannel")
	public PublishSubscribeChannel inboundHelloChannel() {
		return MessageChannels.publishSubscribe(executor).get();
	}

	@Bean(name = "replyHelloChannel")
	public DirectChannel replyHelloChannel() {
		return new DirectChannel();
	}
}