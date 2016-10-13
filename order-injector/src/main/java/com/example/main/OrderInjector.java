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
@EnableScheduling
public class OrderInjector {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("order.exchange");
	}

	@Scheduled(fixedDelay = 1000L)
	public void publishNewOrder() {
		String newOrder = "{id:" + System.currentTimeMillis() + "}";
		System.out.println("Create order : " + newOrder);
		this.rabbitTemplate.convertAndSend("order.exchange", "create.order", newOrder);
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderInjector.class, args)	;
	}
}
