/**
 * 
 */
package com.vidya.flow;

import static java.util.Map.entry;

import java.text.MessageFormat;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.stereotype.Component;

import com.vidya.util.JsonUtil;

/**
 * @author Vidya
 */
@Component
public class PersonFlow {
	
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
					return JsonUtil.toJsonString(Map.ofEntries(entry("personId", personId)));
				})
				.channel("replyPersonChannel")
				.get();
	}
}