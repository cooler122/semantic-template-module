package com.cooler.semantic.service.external;

import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;

import java.util.List;
import java.util.Map;

public interface SimilarityCalculateService {
    /**
     * 全参匹配中，为SVRuleInfo集合计算相似度
     * @param algorithmType     用户选择的算法类型
     * @param svRuleInfos       分词模式绑定的规则集
     * @param ruleId_RRuleEntityDataMap     ruleId下的RRE Map分项数据集
     * @return
     */
    List<SVRuleInfo> similarityCalculate_FPM(Integer algorithmType, List<SVRuleInfo> svRuleInfos, Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap);

    /**
     * 缺参匹配中，为SVRuleInfo计算相似度
     * @param algorithmType     用户选择的算法类型
     * @param historySvRuleInfo 增加匹配实体的历史SVRuleInfo对象
     * @param rRuleEntityMap    已经确定了ruleId的RRE Map
     * @return
     */
    SVRuleInfo similarityCalculate_LPM(Integer algorithmType, SVRuleInfo historySvRuleInfo, Map<String, RRuleEntity> rRuleEntityMap);
}
