package com.finalteam4.danggeunplanner.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;

    public void setValues(String email, String refreshToken, Duration duration){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(email, refreshToken, duration);
    }

    public String getValues(String email){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(email);
    }

    public void delValues(String email){
        redisTemplate.delete(email);
    }

}
