package com.cooler.semantic.service.external;

import com.cooler.semantic.entity.ReferRuleRelation;
import com.cooler.semantic.service.internal.BaseService;

import java.util.List;

public interface ReferRuleRelationService extends BaseService<ReferRuleRelation> {
    /**
     * 根据ruleId获取关联规则关系
     * @param ruleId
     * @return
     */
    List<ReferRuleRelation> selectByRIdAId(Integer ruleId, Integer guideIntentId, Integer accountId);

    /**
     * 根据总的引导意图ID，来查询其下引导的所有ruleId集合。
     * @param guideIntentId
     * @return
     */
    List<Integer> selectRRIdsByIntentId(Integer accountId, Integer guideIntentId);
}
