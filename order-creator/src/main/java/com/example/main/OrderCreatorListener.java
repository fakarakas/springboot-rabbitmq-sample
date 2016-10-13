package com.example.main;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatorListener implements MessageListener {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderCreatorListener(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void onMessage(Message message) {

        String body = new String(message.getBody());

        try
        {
            System.out.println("Create order : " + body);
            this.rabbitTemplate.convertAndSend("order.exchange", "validate.order", body);
        }
        catch(Exception ex) { }
    }

}
