package org.softwareb.seckillservice.config;

import org.softwareb.entity.Order;
import org.softwareb.mapper.OrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@RabbitListener(queues = "user.order.receive_queue")
public class MessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler
    private void receiveMessage(Long oid){
        System.out.println(oid.toString() + " "+ new Date());
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setStatus("1");
        order.setMoney("20");
        order.setId(oid);
        order.setReceiverName("lzy");
        order.setReceiverAddress("hit");
        order.setReceiverPhone("3885189");
        orderMapper.insert(order);
    }
}
