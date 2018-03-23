package com.cooler.semantic.dao;

import com.cooler.semantic.entity.Rule;
import org.apache.ibatis.annotations.Param;

public interface RuleMapper extends BaseMapper<Rule>{

    Double selectAccuracyThresholdByIdAccountId(@Param("accountId") Integer accountId, @Param("ruleId") Integer ruleId);

    Integer selectIntentId(@Param("ruleId") Integer ruleId);
}