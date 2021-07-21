package com.vidya.channels;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.BridgeFrom;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class ChannelConfig {

	@Bean(name = "pubSubChannel")
	public MessageChannel pubSubChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean(name = "directChannel1")
	@BridgeFrom(value = "pubSubChannel")
	public MessageChannel directChannel1() {
		return new DirectChannel();
	}

	@Bean(name = "directChannel2")
	@BridgeFrom(value = "pubSubChannel")
	public MessageChannel directChannel2() {
		return new DirectChannel();
	}
}