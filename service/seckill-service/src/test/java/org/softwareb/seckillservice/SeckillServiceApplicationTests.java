package org.softwareb.seckillservice;

import org.junit.jupiter.api.Test;
import org.softwareb.seckillservice.config.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

@SpringBootTest
class SeckillServiceApplicationTests {

    @Autowired
    private MessageSender messageSender;

    @Test
    void contextLoads() throws InterruptedException {
        messageSender.sendMessage(103L);
        System.out.println("消息发送完成 " + new Date());
        Thread.sleep(20000L);
    }

}
