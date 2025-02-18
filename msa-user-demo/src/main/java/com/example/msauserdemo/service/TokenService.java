package com.example.msauserdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get( email );
    }
    public void saveRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set( email, refreshToken, Duration.ofDays(7));
    }
    public void deleteRefreshToken(String email) {
        redisTemplate.delete( email );
    }
}

