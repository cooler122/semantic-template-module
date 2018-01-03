package com.cooler.semantic.service.external;

import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;

import java.util.List;

public interface RuleSearchService {

    /**
     * 根据句子向量，获取相关的规则集合
     * @param sentenceVectors   句子向量
     * @return  规则集合
     */
    List<SVRuleInfo> getRulesBySentenceVectors(Integer accountId, List<SentenceVector> sentenceVectors);
}
