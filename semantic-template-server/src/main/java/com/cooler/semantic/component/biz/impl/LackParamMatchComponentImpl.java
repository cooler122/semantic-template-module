package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.RedisService;
import com.cooler.semantic.service.external.RuleSearchService;
import com.cooler.semantic.service.external.SimilarityCalculateService;
import com.cooler.semantic.service.internal.AccountConfigurationService;
import com.cooler.semantic.service.internal.RRuleEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("lackParamMatchComponent")
public class LackParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(LackParamMatchComponentImpl.class.getName());
    @Autowired
    private RedisService<SVRuleInfo> redisService;

    public LackParamMatchComponentImpl() {
        super("LPMC", "SO-4 ~ SO-5", "sentenceVectors", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("lackParamMatch.缺参匹配");

        Map<String, REntityWordInfo> rEntityWordInfoMap = new HashMap<>();                                              //此次对话所有分词形式，能提供的所有可能的实体Map
        for (SentenceVector sentenceVector : sentenceVectors) {
            String sentence = sentenceVector.getSentence();
            List<String> words = sentenceVector.getWords();
            List<String> natures = sentenceVector.getNatures();
            List<Double> weights = sentenceVector.getWeights();
            List<List<REntityWordInfo>> rEntityWordInfosList = sentenceVector.getrEntityWordInfosList();
            for (List<REntityWordInfo> rEntityWordInfos : rEntityWordInfosList) {
                for (REntityWordInfo rEntityWordInfo : rEntityWordInfos) {
                    rEntityWordInfoMap.put(rEntityWordInfo.getEntityTypeId(), rEntityWordInfo);
                }
            }
        }

        for(int i = 1; i <= 5; i ++){
            String lastI_OwnerIndex = contextOwner.getLastNOwnerIndex(i);                                               //开始回溯到前面第i轮对话，从前第i轮对话中找半匹配结果
            SVRuleInfo historySvRuleInfo = redisService.getCacheObject(lastI_OwnerIndex + "_" + "optimalSvRuleInfo");
            if(historySvRuleInfo != null) {
                Integer historyRuleId = historySvRuleInfo.getRuleId();                                                  //查出这个半匹配状态下的历史ruleId

                List<RRuleEntity> lackedRRuleEntities = historySvRuleInfo.getLackedRRuleEntities();                     //获得前面n轮的对话状态
                if(lackedRRuleEntities != null && lackedRRuleEntities.size() > 0){

                    //TODO：对前面第i轮结果进行缺参匹配
                    for (RRuleEntity lackedRRuleEntity : lackedRRuleEntities) {                                         //遍历缺失实体集合
                        String entityTypeId = lackedRRuleEntity.getEntityTypeId();                                      //取出某一个缺失实体
                        REntityWordInfo rEntityWordInfo = rEntityWordInfoMap.get(entityTypeId);                         //在本轮对话获取的实体Map中查找，看看能否查找成功
                        if(rEntityWordInfo != null){                                                                    //不为空，则表示查找成功，尝试合并

                        }
                    }

                    return null;
                }
            }
        }



        return null;
    }

}
