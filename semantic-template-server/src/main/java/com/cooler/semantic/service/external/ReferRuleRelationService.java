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
    List<ReferRuleRelation> selectByRuleId(Integer ruleId);
}
