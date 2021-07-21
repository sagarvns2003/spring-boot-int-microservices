package com.vidya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class MulticastingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MulticastingApplication.class, args);
	}
}