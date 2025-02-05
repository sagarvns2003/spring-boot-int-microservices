package com.vidya.server;

import com.vidya.config.AppConfig;
import com.vidya.config.CustomDeserializerSerializer;
import com.vidya.service.MessageService;
import com.vidya.util.CommonUtil;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

/**
 * @author Vidya Sagar Gupta
 * @since v1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TcpServerFlow {
  private final AppConfig appConfig;
  private final MessageChannel fromTcp;
  private final MessageService messageService;

  private static final int REQUEST_MESSAGE_LENGTH_TO_READ = 30;
  private static final CustomDeserializerSerializer CUSTOM_DESERIALIZER_SERIALIZER =
      new CustomDeserializerSerializer(REQUEST_MESSAGE_LENGTH_TO_READ);

  @Bean
  public IntegrationFlow inboundTcpServer() {
    return IntegrationFlow.from(
            Tcp.inboundGateway(
                    Tcp.nioServer(this.appConfig.getTcpPort())
                        .deserializer(CUSTOM_DESERIALIZER_SERIALIZER)
                        .serializer(CUSTOM_DESERIALIZER_SERIALIZER)
                        .soKeepAlive(true)
                        .soTcpNoDelay(true)
                        .backlog(30)
                        .readDelay(5000))
                .requestChannel(this.fromTcp))
        .get();
  }

  @ServiceActivator(inputChannel = "fromTcp", requiresReply = "true")
  public byte[] handleMessage(byte[] messageBody, MessageHeaders messageHeaders) {
    var message = new String(messageBody, StandardCharsets.UTF_8);
    var headers = CommonUtil.extractAllHeaders(messageHeaders);
    return this.messageService.handle(message, headers).getBytes();
  }
}
