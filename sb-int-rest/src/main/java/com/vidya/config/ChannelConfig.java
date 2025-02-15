/** */
package com.vidya.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.BridgeFrom;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

/**
 * @author Vidya
 */
@Configuration
public class ChannelConfig {

  private ExecutorService executor = Executors.newCachedThreadPool();

  @Bean(name = "inboundHealthChannel")
  public PublishSubscribeChannel inboundHealthChannel() {
    return MessageChannels.publishSubscribe(executor).getObject();
  }

  @Bean(name = "replyHealthChannel")
  public DirectChannel replyHealthChannel() {
    return new DirectChannel();
  }

  @Bean(name = "getPersonChannel")
  public PublishSubscribeChannel getPersonChannel() {
    return MessageChannels.publishSubscribe(executor).getObject();
  }

  @Bean(name = "replyPersonChannel")
  public DirectChannel replyPersonChannel() {
    return new DirectChannel();
  }

  @Bean(name = "replyPostPersonChannel")
  public DirectChannel replyPostPersonChannel() {
    return new DirectChannel();
  }

  @Bean(name = "replyPatchPersonChannel")
  public DirectChannel replyPatchPersonChannel() {
    return new DirectChannel();
  }

  @Bean(name = "replyTriggerIntegrationChannel")
  public DirectChannel replyTriggerIntegrationChannel() {
    return new DirectChannel();
  }

  @Bean(name = "transformPersonTriggerChannel")
  public PublishSubscribeChannel transformPersonTriggerChannel() {
    return MessageChannels.publishSubscribe(executor).getObject();
  }

  /* -- Start: Multicasting channel configuration ---- */
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
  /* -- End: Multicasting channel configuration ---- */

}
