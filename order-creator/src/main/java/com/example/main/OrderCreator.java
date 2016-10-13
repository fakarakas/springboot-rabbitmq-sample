package com.example.main;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class OrderCreator {

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("order.exchange");
	}

	// INBOUND
	@Bean
	Queue inboundQueue() {
		return new Queue("create.order.queue", false);
	}

	@Bean
	Binding inboundBinding(Queue inboundQueue, TopicExchange exchange) {
		return BindingBuilder.bind(inboundQueue).to(exchange).with("create.order");
	}

	// LISTENER
	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, Queue inboundQueue, OrderCreatorListener listener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(inboundQueue);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		container.setMessageListener(listener);
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderCreator.class, args)	;
	}
}
