package com.example.main;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrderValidator {

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("order.exchange");
	}

	// INBOUND
	@Bean
	Queue inboundQueue() {
		return new Queue("validate.order.queue", false);
	}

	@Bean
	Binding inboundBinding(Queue inboundQueue, TopicExchange exchange) {
		return BindingBuilder.bind(inboundQueue).to(exchange).with("validate.order");
	}

	// DEAD LETTER
	@Bean
	Queue deadLetterQueue() {
		return new Queue("validate.order.deadletter.queue", false);
	}

	@Bean
	Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange exchange) {
		return BindingBuilder.bind(deadLetterQueue).to(exchange).with("validate.order.error");
	}

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, Queue inboundQueue, OrderMessageListener listener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(inboundQueue);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		container.setMessageListener(listener);
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderValidator.class, args);
	}
}
