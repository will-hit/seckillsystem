package org.softwareb.seckillservice;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.jupiter.api.Test;
import org.softwareb.seckillservice.config.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

@SpringBootTest
class SeckillServiceApplicationTests {



    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() throws InterruptedException {
        Long size = redisTemplate.opsForList().size("SecKillGoods:1");
        System.out.println("size: " + size);
    }

}
