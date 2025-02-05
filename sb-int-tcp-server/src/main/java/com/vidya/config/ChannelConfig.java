package com.vidya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class ChannelConfig {

  @Bean(name = "fromTcp")
  public MessageChannel fromTcp() {
    return new DirectChannel();
  }
}
