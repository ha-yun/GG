package com.example.msalive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 레디스 db에 대한 연결 처리(데이터쓰기, 추출, ...)
 */
@Configuration
public class RedisConfig {
    // 빈을 통해서 레디스 연동 구성
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    // 레디스 데이터를 처리하는 객체를 빈으로 구성
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory()); //연결설정
        // 설정
        template.setKeySerializer(new StringRedisSerializer()); // 키를 저장할 때 문자열 객체 직렬화를 통해서 처리
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 값 객체 직렬화로 저장

        return template;
    }

}