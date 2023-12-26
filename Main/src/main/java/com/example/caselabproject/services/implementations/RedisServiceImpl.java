package com.example.caselabproject.services.implementations;

import com.example.caselabproject.services.RedisService;
import lombok.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean saveToRedis(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Object getFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
