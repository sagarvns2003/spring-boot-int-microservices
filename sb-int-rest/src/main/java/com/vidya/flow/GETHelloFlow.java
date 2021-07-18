/**
 * 
 */
package com.vidya.flow;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.stereotype.Component;

/**
 * @author Vidya
 */
@Component
public class GETHelloFlow {

	@Bean
	public HttpRequestHandlingMessagingGateway getHelloInboundHttp() {
		return Http.inboundGateway("/hello")
				.requestMapping(r -> r.methods(HttpMethod.GET)
						.produces(MediaType.APPLICATION_JSON_VALUE))
				.requestChannel("inboundHelloChannel")
				.replyChannel("replyHelloChannel")
				.replyTimeout(10000)
				.get();
	}

	@ServiceActivator(inputChannel = "inboundHelloChannel")
	public String handleHello() {
		return Map.ofEntries(
				entry("message", "hello")
				).toString();
	}
}