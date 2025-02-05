package com.vidya;

import com.vidya.config.AppConfig;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.integration.config.EnableIntegration;

/**
 * @author Vidya Sagar Gupta
 * @since v1.0.0
 */
@Slf4j
@EnableIntegration
@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TcpServerApplication {

  private final AppConfig appConfig;

  public static void main(String[] args) {
    SpringApplication.run(TcpServerApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    try {
      InetAddress localhost = InetAddress.getLocalHost();
      log.info("===========================================================");
      log.info(
          "=== HTTP Server [IP: {}] started at port: {} ===",
          localhost.getHostAddress(),
          this.appConfig.httpPort());
      log.info(
          "=== TCP  Server [IP: {}] started at port: {} ===",
          localhost.getHostAddress(),
          this.appConfig.getTcpPort());
      log.info("===========================================================");
    } catch (UnknownHostException exp) {
      log.error("Error occurred... {}", exp);
    }
  }
}
