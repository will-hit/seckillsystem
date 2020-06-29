package org.softwareb.seckillservice.task;

import org.apache.zookeeper.data.Id;
import org.softwareb.api.seckill.pojo.MqMessage;
import org.softwareb.common.utils.SnowFlaskUtils;
import org.softwareb.entity.Order;
import org.softwareb.entity.Product;
import org.softwareb.mapper.ProductMapper;
import org.softwareb.seckillservice.config.MessageSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.softwareb.common.constant.OrderStatus.WAIT_PAID;
import static org.softwareb.common.constant.RedisKeyConstant.*;

/***
 * 利用多线程进行订单生成
 */
@Component
public class MultiThreadCreateOrder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SnowFlaskUtils snowFlaskUtils;

    @Autowired
    private MessageSender messageSender;

    @Async("asyncServiceExecutor")
    public void createOrder(){
        Order order = (Order)redisTemplate.boundListOps(GOODSWAITQUEUE).rightPop();
        Integer pid = order.getPid();
        Product product = (Product)redisTemplate.boundHashOps(SECKILLGOODS).get(pid);
        StringBuilder redisKey = new StringBuilder(SECKILLGOODS);
        StringBuilder userQueueStatus = new StringBuilder(USERQUEUESTATUS);
        userQueueStatus.append(":").append(order.getUid()).append(":").append(order.getPid());
        if(product != null){
            redisKey.append(":").append(product.getId());
            Integer one = (Integer) redisTemplate.boundListOps(redisKey.toString()).rightPop();

            // 通过redis串行无锁操作解决不超买问题
            if (one != null){
                StringBuilder orderKey = new StringBuilder(SECKILLORDER);
                order.setId(snowFlaskUtils.nextId());
                order.setStatus(WAIT_PAID);
                order.setCreateTime(new Date());

                orderKey.append(":").append(order.getPid());
                redisTemplate.boundHashOps(orderKey.toString()).put(order.getUid(), order);
                MqMessage message = new MqMessage(order.getUid(), order.getPid());

                // 将消息放入消息队列,用于检查支付状态
                messageSender.sendMessage(message);
                // 添加待支付订单的商品的Id
                StringBuilder IdList = new StringBuilder(USERPIDLIST);
                IdList.append(":").append(order.getUid());
                List Ids = (List)redisTemplate.opsForValue().get(IdList.toString());
                if (Ids == null){
                    List<Integer> list = new ArrayList<>();
                    list.add(order.getPid());
                    redisTemplate.opsForValue().set(IdList.toString(), list);
                }else{
                    Ids.add(order.getPid());
                    redisTemplate.opsForValue().set(IdList.toString(), Ids);
                }

                // 改变状态记录 TODO 宕机了 可能造成某用户无法抢购
                redisTemplate.opsForValue().set(userQueueStatus.toString(), 2, 2, TimeUnit.HOURS);
            }
        }else{
            redisTemplate.opsForValue().set(userQueueStatus.toString(), 0, 2, TimeUnit.HOURS);
        }

    }


}
