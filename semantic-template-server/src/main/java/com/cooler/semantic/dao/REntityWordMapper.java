package com.cooler.semantic.dao;

import com.cooler.semantic.entity.REntityWord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface REntityWordMapper  extends BaseMapper<REntityWord>{

    /**
     * 根据账户ID和词语集查询REntityWord集合
     * @param accountId
     * @param words
     * @return
     */
    List<REntityWord> selectByAIdWords(@Param("accountId") Integer accountId, @Param("words") Set<String> words);
}