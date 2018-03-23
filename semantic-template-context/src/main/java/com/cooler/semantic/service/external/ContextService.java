package com.cooler.semantic.service.external;

import org.springframework.data.redis.core.ValueOperations;
import java.util.List;

public interface ContextService<T> {

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    <T> ValueOperations<String, T> setContext(String key, T value);

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key   缓存的键值
     * @param value 缓存的值
     * @param enableContextLog 是否存储上下文日志
     * @return 缓存的对象
     */
    <T> ValueOperations<String, T> setContext(String key, T value, boolean enableContextLog);

    /**
     * 获得缓存的基本对象。
     * @param key       缓存键值
     * @return 缓存键值对应的数据
     */
    <T> T getContext(String key);

    /**
     * 根据key集合，一次获取多个结果
     * @param keys  key集合
     * @return  多个结果
     */
    List<T> getContextList(List<String> keys);


}
