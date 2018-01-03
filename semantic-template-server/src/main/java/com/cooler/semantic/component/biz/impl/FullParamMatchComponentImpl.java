package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.dao.RRuleEntityMapper;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.RuleSearchService;
import com.cooler.semantic.service.external.SimilarityCalculateService;
import com.cooler.semantic.service.internal.RRuleEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("fullParamMatchComponent")
public class FullParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(FullParamMatchComponentImpl.class.getName());

    @Autowired
    private RuleSearchService ruleSearchService;

    @Autowired
    private RRuleEntityService rRuleEntityService;

    @Autowired
    private SimilarityCalculateService similarityCalculateService;

    public FullParamMatchComponentImpl() {
        super("FPMC", "SO-8 ~ SO-10", "sentenceVectors", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("fullParamMatch.全参匹配");

        System.out.println(JSON.toJSONString(sentenceVectors));
        Integer accountId = contextOwner.getAccountId();
        List<SVRuleInfo> svRuleInfos = ruleSearchService.getRulesBySentenceVectors(accountId, sentenceVectors);
        System.out.println(JSON.toJSONString(svRuleInfos));

        List<RRuleEntity> rRuleEntities = rRuleEntityService.selectBySVRuleInfos(accountId, svRuleInfos);            //这个list理应包含所有SVRuleInfo的所有实体相关数据
        Map<String, RRuleEntity> rRuleEntityDataMap = new HashMap<>();
        for (RRuleEntity rRuleEntity : rRuleEntities) {
            Integer ruleId = rRuleEntity.getRuleId();
            String entityTypeId = rRuleEntity.getEntityTypeId();
            rRuleEntityDataMap.put(ruleId + "_" + entityTypeId, rRuleEntity);                                           //将这些数据放入了Map<ruleId_entityTypeId, RRE>集合中，方便后续取用
        }

        List<SVRuleInfo> svRuleInfosResult = similarityCalculateService.similarityCalculate(accountId, svRuleInfos, rRuleEntityDataMap);
        SVRuleInfo optimalSvRuleInfo = svRuleInfosResult.get(0);

        return new ComponentBizResult("FPMC_S",1, optimalSvRuleInfo);
    }

}
