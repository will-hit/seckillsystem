package org.softwareb.seckillservice.timer;

import org.softwareb.common.constant.RedisKeyConstant;
import org.softwareb.entity.Product;
import org.softwareb.entity.ProductExample;
import org.softwareb.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.softwareb.common.constant.RedisKeyConstant.SECKILLGOODS;

@Component
public class LoadData {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    // 定时从数据库中将秒杀信息读取到redis中 条件:库存>0 且不在redis中
    //（时间区间选取暂时没做）
    // TODO 时间调整
    @Scheduled(cron = "0 0/1 * * * ?")
    public void loadData(){
        System.out.println("正在循环读取");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        StringBuilder redisKey = new StringBuilder(SECKILLGOODS);
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria =example.createCriteria();
        criteria.andStockCountGreaterThan(0);
        // 暂时没有添加时间逻辑
        Set keys =redisTemplate.boundHashOps(redisKey.toString()).keys();
        List<Integer> list = new ArrayList<>(keys);
        if(list != null && list.size() > 0)criteria.andIdNotIn(list);
        List<Product> products = productMapper.selectByExample(example);
        for (Product product : products) {
            System.out.println(product);
            redisTemplate.boundHashOps(SECKILLGOODS).put(product.getId(), product);
            // 读入redis库存用于分布式锁
            StringBuilder stockredisKey = new StringBuilder(SECKILLGOODS);
            stockredisKey.append(":").append(product.getId());
            for (int stock = product.getStockCount(); stock > 0; stock--){
                redisTemplate.opsForList().leftPush(stockredisKey.toString(),1);
            }
        }
    }
}
