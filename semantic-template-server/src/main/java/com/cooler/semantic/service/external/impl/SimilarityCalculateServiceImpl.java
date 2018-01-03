package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.SimilarityCalculateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("similarityCalculateService")
public class SimilarityCalculateServiceImpl implements SimilarityCalculateService {

    @Override
    public List<SVRuleInfo> similarityCalculate(Integer accountId, List<SVRuleInfo> svRuleInfos, Map<String, RRuleEntity> rRuleEntityMap) {
        System.out.println(accountId);
        //TODO:引入各个相似度计算算法
        return null;
    }
}
