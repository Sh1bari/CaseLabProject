package com.example.caselabproject.services;

import lombok.*;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
public interface RedisService {

    boolean saveToRedis(String key, Object value);

    Object getFromRedis(String key);
}
