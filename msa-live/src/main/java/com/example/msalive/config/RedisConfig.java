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
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;


    // 빈을 통해서 레디스 연동 구성

    // 레디스와 연동 관리하는 객체를 빈으로 구성
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 연결 클라이언트 객체를 생성 반환
        // (*)LettuceConnectionFactory, JedisConnectionFactory
        // 환경 설정 정보에 있는 내용을 기반으로 연결
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