package com.cooler.semantic.dao;

import com.cooler.semantic.entity.ReferRuleCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReferRuleConditionMapper extends BaseMapper<ReferRuleCondition>{

    List<ReferRuleCondition> selectByRRRelationId(@Param("referRuleRelationId") Integer referRuleRelationId);
}