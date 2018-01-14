package com.cooler.semantic.dao;

import com.cooler.semantic.entity.AnaphoraWord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface AnaphoraWordMapper extends BaseMapper<AnaphoraWord>{
    /**
     * 检测传入的words是不是指代词语
     * @param words
     * @return
     */
    List<AnaphoraWord> selectByWords(@Param("words") Set<String> words);
}