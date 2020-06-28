package org.softwareb.seckillservice.service;


import com.alibaba.dubbo.config.annotation.Service;
import org.softwareb.api.seckill.ISeckillService;
import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Order;
import org.softwareb.seckillservice.task.MultiThreadCreateOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.softwareb.common.constant.RedisKeyConstant.GOODSWAITQUEUE;
import static org.softwareb.common.constant.RedisKeyConstant.USERQUEUESTATUS;

@Service
@Component
public class SeckillService implements ISeckillService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MultiThreadCreateOrder multiThreadCreateOrder;

    // order中需要包含 uid pid createTime phone address
    @Override
    public ResultBean addTask(Order order) {
        StringBuilder redisKeys = new StringBuilder(USERQUEUESTATUS);
        redisKeys.append(":").append(order.getId()).append(":").append(order.getPid());
        // 判断能不能进行下单
        // TODO try-catch捕获异常
        Integer o = (Integer)redisTemplate.opsForValue().get(redisKeys.toString());
        if (o == null || o.intValue() == 1){
            redisTemplate.boundListOps(GOODSWAITQUEUE).leftPush(order);
            // 设置两小时过期时间
            redisTemplate.opsForValue().set(redisKeys.toString(), 1, 2, TimeUnit.HOURS);
            //异步下单操作
            multiThreadCreateOrder.createOrder();
            return ResultBean.success("正在抢单");
        }else {
            return ResultBean.error("不可重复抢单", 500);
        }
    }
}
