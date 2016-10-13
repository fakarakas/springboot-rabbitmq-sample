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
public class OrderEmailer implements MessageListener {

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("order.exchange");
	}

	// INBOUND
	@Bean
	Queue inboundQueue() {
		return new Queue("email.queue", false);
	}

	@Bean
	Binding inboundBinding(Queue inboundQueue, TopicExchange exchange) {
		return BindingBuilder.bind(inboundQueue).to(exchange).with("finalize.*");
	}

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, Queue inboundQueue) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(inboundQueue);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		container.setMessageListener(new OrderEmailer());
		return container;
	}

	@Override
	public void onMessage(Message message) {

		String body = new String(message.getBody());
		System.out.println("Sending email for order : " + body);
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderEmailer.class, args)	;
	}
}
