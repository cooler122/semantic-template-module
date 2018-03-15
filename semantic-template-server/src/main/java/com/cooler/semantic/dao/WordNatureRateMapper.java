package com.cooler.semantic.dao;

import com.cooler.semantic.entity.WordCN;
import com.cooler.semantic.entity.WordNatureRate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WordNatureRateMapper extends BaseMapper<WordCN>{

    /**
     * 根据accountId来查询用户自定义的词性权重率
     * @param accountId
     * @return  自定义词性权重率
     */
    List<WordNatureRate> selectByAccountId(@Param("accountId") Integer accountId);
}