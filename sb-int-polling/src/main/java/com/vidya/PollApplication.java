package com.vidya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class PollApplication {
  public static void main(String[] args) {
    SpringApplication.run(PollApplication.class, args);
  }
}
