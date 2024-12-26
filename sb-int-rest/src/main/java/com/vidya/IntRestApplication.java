package com.vidya;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.http.config.EnableIntegrationGraphController;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@Configuration
@EnableRetry
@EnableAsync
@EnableWebSecurity
@EnableIntegration
@IntegrationComponentScan
@EnableIntegrationGraphController(allowedOrigins = "http://localhost:8082")
@EnableIntegrationManagement
public class IntRestApplication {

  public static void main(String[] args) {
    SpringApplication.run(IntRestApplication.class, args);
  }

  @PostConstruct
  private void setTimeZone() throws Exception {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    // LeaderElectionService node1 = new LeaderElectionService(8888, "testChannelName", "localhost",
    //		List.of(/* "localhost[" + 8888 + "]", "localhost[" + 8888 + "]" */));
    // node1.connect();
  }
}
