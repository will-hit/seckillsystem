package org.softwareb.seckillservice.task;

import org.softwareb.common.utils.SnowFlaskUtils;
import org.softwareb.entity.Order;
import org.softwareb.entity.Product;
import org.softwareb.mapper.ProductMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.softwareb.common.constant.RedisKeyConstant.GOODSWAITQUEUE;
import static org.softwareb.common.constant.RedisKeyConstant.SECKILLGOODS;
import static org.softwareb.common.constant.RedisKeyConstant.SECKILLORDER;
import static org.softwareb.common.constant.OrderStatus.WAIT_PAID;
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
    private RabbitTemplate rabbitTemplate;

    @Async("asyncServiceExecutor")
    public void createOrder(){
        Order order = (Order)redisTemplate.boundListOps(GOODSWAITQUEUE).rightPop();
        Integer pid = order.getPid();
        Product product = (Product)redisTemplate.boundHashOps(SECKILLGOODS).get(pid);
        StringBuilder redisKey = new StringBuilder(SECKILLGOODS);
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
                // 改变状态记录 TODO 宕机了 可能造成某用户无法抢购
                redisTemplate.opsForValue().set(order.getId(), 2, 2, TimeUnit.HOURS);
            }
        }else{
            redisTemplate.opsForValue().set(order.getId(), 0, 2, TimeUnit.HOURS);
        }

    }


}
