package org.softwareb.seckillservice.config;

import org.softwareb.common.utils.SnowFlaskUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowFlaskConfig {

    @Value("${snowflask.workerId}")
    private Long workerId;

    @Value("${snowflask.datacenterId}")
    private Long datacenterId;

    @Bean
    public SnowFlaskUtils snowFlaskUtils(){
        SnowFlaskUtils snowFlaskUtils = new SnowFlaskUtils(workerId, datacenterId);
        return snowFlaskUtils;
    }
}
