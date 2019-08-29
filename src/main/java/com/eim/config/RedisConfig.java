package com.eim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    public String REDIS_HOST;

    @Value("${spring.redis.port}")
    public int REDIS_PORT;

    @Value("${spring.redis.password}")
    public String REDIS_PASSWORD;
}
