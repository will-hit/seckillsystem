package org.softwareb.seckillservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SeckillServiceApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        String redisKey = "Test111111";
        redisTemplate.opsForValue().set(redisKey, 1);
        redisTemplate.boundListOps(redisKey).leftPush(1);
        Integer o = (Integer) redisTemplate.boundListOps(redisKey).rightPop();
        if(o == null){
            System.out.println("null");
        }else{
            System.out.println(o.intValue());
        }
        o = (Integer) redisTemplate.boundListOps(redisKey).rightPop();
        if(o == null){
            System.out.println("null");
        }else{
            System.out.println(o.intValue());
        }
    }

}
