/**
 * 
 */
package com.vidya.flow;

import static java.util.Map.entry;

import java.text.MessageFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Vidya
 */
@Component
public class PersonFlow {

	@Autowired
	private ObjectMapper objectMapper;
	
	/*
	 * GET
	 * PathVariable example
	 */
	@Bean
	public HttpRequestHandlingMessagingGateway getPersonInboundHttp() {
		return Http.inboundGateway("/person/{personId}")
				.requestMapping(r -> r.methods(HttpMethod.GET).produces(MediaType.APPLICATION_JSON_VALUE))
				.payloadExpression("#pathVariables.personId")
				.requestChannel("getPersonChannel")
				.replyChannel("replyPersonChannel")
				.replyTimeout(20000)
				.get();
	}

	@Bean
	public IntegrationFlow getPerson() {
		return IntegrationFlow
				.from("getPersonChannel")
				.log(LoggingHandler.Level.INFO, message -> MessageFormat.format("Get persion details for person id: {0}", message.getPayload()))
				.handle((payload, headers) -> {
					String personId = (String) payload;
					try {
						return objectMapper.writeValueAsString(Map.ofEntries(entry("personId", personId)));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						return "{}";
					}
				})
				.channel("replyPersonChannel")
				.get();
	}
}