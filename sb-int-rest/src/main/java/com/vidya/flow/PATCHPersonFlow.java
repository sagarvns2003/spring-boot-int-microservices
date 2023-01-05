/**
 * 
 */
package com.vidya.flow;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.ConsumerEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.stereotype.Component;

import com.vidya.dao.entity.Person;

import jakarta.persistence.EntityManager;

/**
 * @author Vidya
 */
@Component
public class PATCHPersonFlow {
	
	@Autowired
	private EntityManager entityManager;
	
	/*
	 * Patch
	 * Update person example
	 */
	@Bean
	public HttpRequestHandlingMessagingGateway patchPersonInboundHttp() {
		return Http.inboundGateway("/person")
				.requestMapping(r -> r.methods(HttpMethod.PATCH)
						              .consumes(MediaType.APPLICATION_JSON_VALUE)
						              .produces(MediaType.APPLICATION_JSON_VALUE))
				.requestPayloadType(Person.class) //Convert request body to class type
				.requestChannel("patchPersonInboundChannel")
				.replyChannel("replyPatchPersonChannel")
				.replyTimeout(20000)
				.get();
	}
	
	@Bean
	public IntegrationFlow updatePerson() {
		return IntegrationFlow
				.from("patchPersonInboundChannel")
				.log(LoggingHandler.Level.INFO, message -> MessageFormat.format("Request to update person... {0}", message.getPayload()))
				.handle(Jpa.updatingGateway(entityManager)
						.persistMode(PersistMode.MERGE)
						.entityClass(Person.class), ConsumerEndpointSpec::transactional)
				.log(LoggingHandler.Level.INFO, message -> "Person updated... " + message.getPayload())
				.channel("replyPatchPersonChannel")
				.get();
	}
}