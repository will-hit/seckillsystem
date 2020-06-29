package org.softwareb.seckillservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitmqConfig {

    // 死信交换机
    @Bean
    public DirectExchange userOrderDelayExchange(){
        return new DirectExchange("user.order.delay_exchange");
    }

    // 死信队列
    @Bean
    public Queue userOrderDelayQueue(){
        Map<String, Object> map = new HashMap<>(16);
        map.put("x-dead-letter-exchange", "user.order.receive_exchange");
        map.put("x-dead-letter-routing-key", "user.order.receive_key");
        return new Queue("user.order.delay_queue", true, false, false, map);
    }

    // 绑定交换机
    @Bean
    public Binding userOrderDelayBinding(DirectExchange userOrderDelayExchange, Queue userOrderDelayQueue){
        return BindingBuilder
                .bind(userOrderDelayQueue)
                .to(userOrderDelayExchange)
                .with("user.order.delay_exchange");
    }

    // 死信接受交换机
    @Bean
    public DirectExchange userOrderReceiveExchange() {
        return new DirectExchange("user.order.receive_exchange");
    }

    // 死信接受队列
    @Bean
    public Queue userOrderReceiveQueue() {
        return new Queue("user.order.receive_queue");
    }

    @Bean
    public Binding userOrderReceiveBinding(DirectExchange userOrderReceiveExchange, Queue userOrderReceiveQueue){
        return BindingBuilder
                .bind(userOrderReceiveQueue)
                .to(userOrderReceiveExchange)
                .with("user.order.receive_key");
    }

}
