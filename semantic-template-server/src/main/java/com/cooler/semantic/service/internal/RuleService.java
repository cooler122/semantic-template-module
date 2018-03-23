package com.cooler.semantic.service.internal;

import com.cooler.semantic.entity.Rule;

import java.util.List;
import java.util.Map;

public interface RuleService extends BaseService<Rule> {

    /**
     * 根据accountId和optimalRuleId来查询此rule的私有阈值
     * @param accountId
     * @param ruleId
     * @return
     */
    Double selectAccuracyThresholdByIdAccountId(Integer accountId, Integer ruleId);

    /**
     * 根据ruleId查询意图ID
     * @param ruleId
     * @return
     */
    Integer selectIntentId(Integer ruleId);
}
