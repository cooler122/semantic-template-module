package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.service.external.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("contextService")
public class ContextServiceImpl<T> implements ContextService<T> {

    @Autowired
    @Qualifier("jedisTemplate")
    public RedisTemplate redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setContext(String key, T value) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        return operation;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key   缓存的键值
     * @param value 缓存的值
     * @param enableContextLog 是否存储上下文日志
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setContext(String key, T value, boolean enableContextLog) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        //TODO: 另起一个线程存储上下文日志

        return operation;
    }

    /**
     * 获得缓存的基本对象。
     * @param key       缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getContext(String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 根据key集合，一次获取多个结果
     * @param keys  key集合
     * @return  多个结果
     */
    public List<T> getContextList(List<String> keys) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.multiGet(keys);
    }

}