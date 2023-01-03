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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vidya.util.JsonUtil;

/**
 * @author Vidya
 */
@Component
public class GETHealthFlow {

	@Bean
	public HttpRequestHandlingMessagingGateway getHealthInboundHttp() {
		return Http.inboundGateway("/health")
				.requestMapping(r -> r.methods(HttpMethod.GET)
				.produces(MediaType.APPLICATION_JSON_VALUE))
				.requestChannel("inboundHealthChannel")
				.replyChannel("replyHealthChannel")
				.replyTimeout(30000)
				.get();
	}

	@ServiceActivator(inputChannel = "inboundHealthChannel")
	public String handleHealth() throws JsonProcessingException {
		return JsonUtil.toJsonString(Map.ofEntries(entry("isAvailable", true)));
	}
}