package com.cooler.semantic.service.external;

import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;

import java.util.List;
import java.util.Map;

public interface SimilarityCalculateService {
    /**
     * 为SVRuleInfo集合计算相似度
     * @param svRuleInfos
     */
    List<SVRuleInfo> similarityCalculate(ContextOwner contextOwner, List<SVRuleInfo> svRuleInfos, Map<String, RRuleEntity> rRuleEntityMap);
}
