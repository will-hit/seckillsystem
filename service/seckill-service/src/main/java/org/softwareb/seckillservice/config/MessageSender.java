package org.softwareb.seckillservice.config;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Long message){
        rabbitTemplate.convertAndSend("user.order.delay_exchange", "user.order.delay_exchange",
                message, new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        // 设置过期时间为10s
                        message.getMessageProperties().setExpiration("10000");
                        return message;
                    }
                });
    }
}
