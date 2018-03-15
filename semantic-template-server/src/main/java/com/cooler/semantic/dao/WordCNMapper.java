package com.cooler.semantic.dao;

import com.cooler.semantic.entity.WordCN;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface WordCNMapper extends BaseMapper<WordCN> {

    /**
     * 根据word字符串集合来查找相关的WordCN对象集合
     * @param words
     * @return
     */
    List<WordCN> selectByWords(@Param("words") Set<String> words);
}