package com.example.main;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageListener implements MessageListener {

    private int count = 1;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public OrderMessageListener(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void onMessage(Message message) {

        String body = new String(message.getBody());

        try
        {
            System.out.println("Validate order : " + body);
            Thread.sleep(1000);
            if(count++ % 10 == 0) throw new Exception("Failed !!!");
            this.rabbitTemplate.convertAndSend("order.exchange", "finalize.order", body);
        }
        catch(Exception ex)
        {
            System.out.println("ERROR");
            this.rabbitTemplate.convertAndSend("order.exchange", "validate.order.error", body);
        }
    }

}
