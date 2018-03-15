package com.cooler.semantic.service.external;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService<T> {
    <T> ValueOperations<String, T> setCacheObject(String key, T value);
    /**
     * 获得缓存的基本对象。
     * @param key       缓存键值
     * @return 缓存键值对应的数据
     */
    <T> T getCacheObject(String key/*,ValueOperations<String,T> operation*/);

    /**
     * 根据key集合，一次获取多个结果
     * @param keys  key集合
     * @return  多个结果
     */
    List<T> getCacheObjects(List<String> keys);

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    <T> ListOperations<String, T> setCacheList(String key, List<T> dataList);

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    <T> List<T> getCacheList(String key) ;

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet);

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    Set<T> getCacheSet(String key);

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap);

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    <T> Map<String, T> getCacheMap(String key);


    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    <T> HashOperations<String, Integer, T> setCacheIntegerMap(String key, Map<Integer, T> dataMap);

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    <T> Map<Integer, T> getCacheIntegerMap(String key);


}
