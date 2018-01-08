package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponentBase;
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
    @Autowired
    private SimilarityCalculateService similarityCalculateService;
    @Autowired
    private AccountConfigurationService accountConfigurationService;

    public LackParamMatchComponentImpl() {
        super("LPMC", "SO-4 ~ SO-5", "sentenceVectors", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("lackParamMatch.缺参匹配");
        AccountConfiguration accountConfiguration = accountConfigurationService.selectAIdUId(contextOwner.getAccountId(), contextOwner.getUserId());
        Integer algorithmType = accountConfiguration.getAlgorithmType();                                                //算法类型

        Map<String, REntityWordInfo> rEntityWordInfoMap = new HashMap<>();                                              //此次对话所有分词形式，能提供的所有可能的实体Map
        for (SentenceVector sentenceVector : sentenceVectors) {
            List<List<REntityWordInfo>> rEntityWordInfosList = sentenceVector.getrEntityWordInfosList();
            for (List<REntityWordInfo> rEntityWordInfos : rEntityWordInfosList) {
                for (REntityWordInfo rEntityWordInfo : rEntityWordInfos) {
                    rEntityWordInfoMap.put(rEntityWordInfo.getEntityTypeId(), rEntityWordInfo);
                }
            }
        }
//TODO:此处没处理好！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        for(int i = 0; i < 5; i ++){
            String lastI_OwnerIndex = contextOwner.getLastNOwnerIndex(i);                                               //开始回溯到前面第i轮对话，从前第i轮对话中找半匹配结果
            DataComponentBase<SVRuleInfo> historyData = redisService.getCacheObject(lastI_OwnerIndex + "_" + "optimalSvRuleInfo");
            SVRuleInfo historySvRuleInfo = historyData.getData();
            if(historySvRuleInfo != null) {
                List<REntityWordInfo> historyMatchedREntityWordInfos = historySvRuleInfo.getMatchedREntityWordInfos();  //第i轮对话，匹配上的REW集合
                List<RRuleEntity> historyMatchedRRuleEntities = historySvRuleInfo.getMatchedRRuleEntities();            //第i轮对话，匹配上的RRE集合
                List<RRuleEntity> historyLackedRRuleEntities = historySvRuleInfo.getLackedRRuleEntities();              //第i轮对话，缺失的RRE集合

                if(historyLackedRRuleEntities != null && historyLackedRRuleEntities.size() > 0){
                    for(int j = 0; j < historyLackedRRuleEntities.size(); j ++){                                        //遍历缺失实体集合
                        RRuleEntity historyLackedRRuleEntity = historyLackedRRuleEntities.get(j);
                        String entityTypeId = historyLackedRRuleEntity.getEntityTypeId();                               //取出某一个缺失实体
                        REntityWordInfo rEntityWordInfo = rEntityWordInfoMap.get(entityTypeId);                         //在本轮对话获取的实体Map中查找，看看能否查找成功
                        if(rEntityWordInfo != null){                                                                   //不为空，则表示查找成功，尝试合并
                            Double sv_weight = rEntityWordInfo.getWeight();                                             //本次句子分词段的权重
                            Double rule_weight = historyLackedRRuleEntity.getWeight();                                  //第i轮对话中缺失实体的权重

                            historyMatchedREntityWordInfos.add(rEntityWordInfo);                                        //添加这个REW
                            historyMatchedRRuleEntities.add(historyLackedRRuleEntity);                                  //添加这个RRE
                        }
                    }
                    historyLackedRRuleEntities.removeAll(historyMatchedRRuleEntities);                                  //删除所有匹配上的RRE

                    if(historyMatchedRRuleEntities != null && historyMatchedRRuleEntities.size() > 0){
                        Map<String, RRuleEntity> rRuleEntityMap = new HashMap<>();
                        for (RRuleEntity historyMatchedRRuleEntity : historyMatchedRRuleEntities) {
                            String entityTypeId = historyMatchedRRuleEntity.getEntityTypeId();
                            rRuleEntityMap.put(entityTypeId, historyMatchedRRuleEntity);
                        }
                        SVRuleInfo historySVRuleInfo = similarityCalculateService.similarityCalculate_LPM(algorithmType, historySvRuleInfo, rRuleEntityMap);      //此输出参数只有一个结果
                        System.out.println(historySVRuleInfo);
                    }

                }
            }
        }



        return null;
    }

}
