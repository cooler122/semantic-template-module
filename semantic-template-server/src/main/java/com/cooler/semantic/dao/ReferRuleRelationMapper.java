package com.cooler.semantic.dao;

import com.cooler.semantic.entity.ReferRuleRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReferRuleRelationMapper extends BaseMapper<ReferRuleRelation> {

    List<ReferRuleRelation> selectByRuleId(@Param("ruleId") Integer ruleId);
}