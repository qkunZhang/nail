package com.back.util.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private final RedisTemplate<String, Object> redis;

    public RedisUtil(RedisTemplate<String, Object> redis) {
        this.redis = redis;
    }

    public boolean set(String key, Object value) {
        try {
            ValueOperations<String, Object> operations = redis.opsForValue();
            operations.set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setWithExpire(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            ValueOperations<String, Object> operations = redis.opsForValue();
            operations.set(key, value, timeout, timeUnit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object get(String key) {
        try {
            ValueOperations<String, Object> operations = redis.opsForValue();
            return operations.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(String key) {
        try {
            return Boolean.TRUE.equals(redis.delete(key));
        } catch (Exception e) {
            return false;
        }
    }

    public Long incr(String key, long delta) {
        try {
            ValueOperations<String, Object> operations = redis.opsForValue();
            return operations.increment(key, delta);
        } catch (Exception e) {
            return null;
        }
    }

    public Long decr(String key, long delta) {
        try {
            ValueOperations<String, Object> operations = redis.opsForValue();
            return operations.decrement(key, delta);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redis.hasKey(key));
    }
}