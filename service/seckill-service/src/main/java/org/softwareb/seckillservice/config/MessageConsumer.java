package org.softwareb.seckillservice.config;

import org.softwareb.api.seckill.pojo.MqMessage;
import org.softwareb.entity.Order;
import org.softwareb.mapper.OrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

import static org.softwareb.common.constant.RedisKeyConstant.*;

@Configuration
@RabbitListener(queues = "user.order.receive_queue")
public class MessageConsumer {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitHandler
    private void receiveMessage(MqMessage message){
        System.out.println(message);
        StringBuilder orderkey = new StringBuilder(SECKILLORDER);
        orderkey.append(":").append(message.getPid());
        Order order = (Order)redisTemplate.boundHashOps(orderkey.toString()).get(message.getUid());
//        if (order == null)
        Order orderDB = orderMapper.selectByPrimaryKey(order.getId());
        // order.status "0": 无效订单 "1":待付款 "2"：待发货 "3"：待收货 "4"：待评价
        // 未按时付款 进行回滚操作
        if (orderDB == null || "0".equals(orderDB.getStatus())){
            // 恢复库存
            StringBuilder seckillgoods = new StringBuilder(SECKILLGOODS);
            seckillgoods.append(":").append(message.getPid());
            redisTemplate.boundListOps(seckillgoods.toString()).leftPush(1);
        }

        // 删除待付款订单
        redisTemplate.boundHashOps(orderkey.toString()).delete(message.getUid());
        // 删除待支付订单的商品Id
        StringBuilder IdList = new StringBuilder(USERPIDLIST);
        IdList.append(":").append(order.getUid());
        List Ids = (List)redisTemplate.opsForValue().get(IdList.toString());
        if (Ids != null){
            Ids.remove(order.getId());
            redisTemplate.opsForValue().set(IdList.toString(), Ids);
        }
        // 删除排队状态
        StringBuilder userQueueStatus = new StringBuilder(USERQUEUESTATUS);
        userQueueStatus.append(":").append(message.getUid()).append(":").append(message.getPid());
        redisTemplate.delete(userQueueStatus.toString());

    }
}
