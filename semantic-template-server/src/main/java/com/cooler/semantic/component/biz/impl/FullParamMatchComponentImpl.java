package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.RuleSearchService;
import com.cooler.semantic.service.external.SimilarityCalculateService;
import com.cooler.semantic.service.internal.AccountConfigurationService;
import com.cooler.semantic.service.internal.RRuleEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("fullParamMatchComponent")
public class FullParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(FullParamMatchComponentImpl.class.getName());

    @Autowired
    private RuleSearchService ruleSearchService;
    @Autowired
    private RRuleEntityService rRuleEntityService;
    @Autowired
    private SimilarityCalculateService similarityCalculateService;
    @Autowired
    private AccountConfigurationService accountConfigurationService;

    public FullParamMatchComponentImpl() {
        super("FPMC", "SO-8 ~ SO-10", "sentenceVectors", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.debug("全参匹配");
        AccountConfiguration accountConfiguration = accountConfigurationService.selectAIdUId(contextOwner.getAccountId(), contextOwner.getUserId());

        //1.通过各个分词段检索出的实体，将各个实体对应的rule数据（这一步主要获得各个实体对应的ruleId）检索出来，通过预估值计算，提取最佳的前5位规则，并封装成SVRuleInfo集合返回出来
        List<SVRuleInfo> svRuleInfos = ruleSearchService.getRulesBySentenceVectors(contextOwner, sentenceVectors);
        System.out.println(JSON.toJSONString(svRuleInfos));
        if(svRuleInfos != null && svRuleInfos.size() > 0){
            //2.通过svRuleInfo里面的ruleId，将每一个rule-entity关联数据检索出来，以备后续计算相似度
            List<RRuleEntity> rRuleEntities = rRuleEntityService.selectBySVRuleInfos(contextOwner.getAccountId(), svRuleInfos);            //这个list理应包含所有SVRuleInfo的所有实体相关数据
            Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap = new HashMap<>();
            for (RRuleEntity rRuleEntity : rRuleEntities) {
                Integer ruleId = rRuleEntity.getRuleId();                                                                   //由于前面getRulesBySentenceVectors方法里面做过限定，此ruleId不会超过5个，所以下面新建里的小Map不会超过5个
                Map<String, RRuleEntity> rRuleEntityMap = ruleId_RRuleEntityDataMap.get(ruleId);
                if(rRuleEntityMap == null){
                    rRuleEntityMap = new HashMap<>();
                }
                String entityTypeId = rRuleEntity.getEntityTypeId();
                rRuleEntityMap.put(entityTypeId, rRuleEntity);                                                              //将这些数据放入了Map<entityTypeId, RRE>集合中，方便后续取用
                ruleId_RRuleEntityDataMap.put(ruleId, rRuleEntityMap);                                                      //将设置好值得Map放到大Map中Map<ruleId, Map<entityTypeId, RRuleEntity>>
            }

            //3.计算相似度，并选择最优集合
            Integer algorithmType = accountConfiguration.getAlgorithmType();                                                //由用户选择使用哪种算法（当前1~5种， 默认JACCARD_VOLUME_WEIGHT_RATE）
            List<SVRuleInfo> svRuleInfosResult = similarityCalculateService.similarityCalculate_FPM(algorithmType, svRuleInfos, ruleId_RRuleEntityDataMap); //svRuleInfosResult不可能为null，且传进去多少，也返回多少
            Collections.sort(svRuleInfosResult, new Comparator<SVRuleInfo>() {
                @Override
                public int compare(SVRuleInfo o1, SVRuleInfo o2) {                                                        //倒序排序，见"if(similarity1 > similarity2) return -1;"
                    Double similarity1 = o1.getSimilarity();
                    Double similarity2 = o2.getSimilarity();
                    if(similarity1 > similarity2) return -1;
                    else if(similarity1 < similarity2) return 1;
                    else return 0;
                }
            });
            SVRuleInfo optimalSvRuleInfo = svRuleInfosResult.get(0);                                                        //获取相似度值最大的那一个（最优结果）
            optimalSvRuleInfo.setrEntityWordInfosList(null);                                                                //最优规则找到后，需要保存（本地、远程），此过程数据太大而且后续没有更多作用，故此处设置为null

            Double accuracyThreshold = accountConfiguration.getAccuracyThreshold();
            if(optimalSvRuleInfo.getSimilarity() > accuracyThreshold){
                return new ComponentBizResult("FPMC_S", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);      //此结果在本地和远程都要存储
            }else{
                return new ComponentBizResult("FPMC_F", "FPMC_F_SimilarityNotBeyond");      //此结果在本地和远程都要存储
            }
        }
        return new ComponentBizResult("FPMC_F", "FPMC_F_NoMatchRule");      //此结果在本地和远程都要存储
    }
}
