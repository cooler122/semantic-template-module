package com.cooler.semantic.dao;

import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.REntityWordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RRuleEntityMapper extends BaseMapper<RRuleEntity> {
    /**
     * 根据rEntityWordInfos结果来获取RRuleEntity对象集
     * @param accountId
     * @param rEntityWordInfos
     * @return
     */
    List<RRuleEntity> selectByREntityWordInfos(@Param("accountId") Integer accountId, @Param("rEntityWordInfos") List<REntityWordInfo> rEntityWordInfos);
}