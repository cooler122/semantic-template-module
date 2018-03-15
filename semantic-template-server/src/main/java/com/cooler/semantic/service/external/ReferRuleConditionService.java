package com.cooler.semantic.service.external;

import com.cooler.semantic.entity.ReferRuleCondition;
import com.cooler.semantic.service.internal.BaseService;

import java.util.List;

public interface ReferRuleConditionService extends BaseService<ReferRuleCondition> {
    /**
     * 根据refer_rule_relation的ID获取关联规则关系
     * @param referRuleRelationId
     * @return
     */
    List<ReferRuleCondition> selectByRRRelationId(Integer referRuleRelationId);
}
