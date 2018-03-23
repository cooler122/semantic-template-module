package com.cooler.semantic.dao;

import com.cooler.semantic.entity.ReferRuleRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReferRuleRelationMapper extends BaseMapper<ReferRuleRelation> {

    List<ReferRuleRelation> selectByRIdAId(@Param("ruleId") Integer ruleId, @Param("guideIntentId") Integer guideIntentId, @Param("accountId") Integer accountId);

    List<Integer> selectRRIdsByIntentId(@Param("accountId") Integer accountId, @Param("guideIntentId") Integer guideIntentId);
}