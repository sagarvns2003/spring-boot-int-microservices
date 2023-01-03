/**
 * 
 */
package com.vidya.flow;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.vidya.dao.entity.Person;
import com.vidya.dao.repository.PersonRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

/**
 * @author Vidya
 */
@Component
public class GETPersonFlow {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private EntityManager entityManager;
	
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
					Integer personId = Integer.valueOf((String)payload);
					Optional<Person> person = this.personRepository.findById(personId);
					if (person.isPresent()) {
						return person.get();
					} else {
						return "{}";
					}
				})
				.channel("replyPersonChannel")
				.get();
	}
	
	/*
	@SuppressWarnings("unchecked")
	@Transactional
	@ServiceActivator(inputChannel = "getPersonChannel", outputChannel = "replyPersonChannel")
	public Message<?> getPerson(Message<?> message) throws ExecutionException, InterruptedException {
		Optional<Person> person = this.entityManager.createQuery("from Person p where p.id = :id ")
				.setParameter("id", message.getPayload()).getResultStream().findFirst();
		if (person.isPresent()) {
			return MessageBuilder.withPayload(person.get()).build();
		} else {
			return MessageBuilder.withPayload("{}").build();
		}
	}*/
}