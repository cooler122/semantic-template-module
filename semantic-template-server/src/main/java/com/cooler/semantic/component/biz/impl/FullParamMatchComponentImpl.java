package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.CalculationLogParam_FPM;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.RuleSearchService;
import com.cooler.semantic.service.external.SimilarityCalculateService;
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

    public FullParamMatchComponentImpl() {
        super("FPMC", "sentenceVectors", "optimalSvRuleInfo_FPM");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.trace("FPMC.全参匹配");

        DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = dataComponent.getData();
        int calculationLogType = request.getCalculationLogType();
        int algorithmType = request.getAlgorithmType();                                                                 //由用户选择使用哪种算法（当前1~5种， 默认JACCARD_VOLUME_WEIGHT_RATE）

        CalculationLogParam_FPM calculationLogParam_fpm = null;
        if(calculationLogType != Constant.NO_CALCULATION_LOG){
            calculationLogParam_fpm = new CalculationLogParam_FPM();
        }

        //1.通过各个分词段检索出的实体，将各个实体对应的rule数据（这一步主要获得各个实体对应的ruleId）检索出来，通过预估值计算，提取最佳的前5位规则，并封装成SVRuleInfo集合返回出来
        List<SVRuleInfo> fmpTop5SvRuleInfos = ruleSearchService.getRulesBySentenceVectors(contextOwner, sentenceVectors, calculationLogParam_fpm);

        if(fmpTop5SvRuleInfos != null && fmpTop5SvRuleInfos.size() > 0){
            //2.通过svRuleInfo里面的ruleId，将每一个规则端的RRE关联数据检索出来，以备后续计算相似度（和前面预选择不同，前面预选择计算的是SV端的RRE数据）
            List<RRuleEntity> rRuleEntities = rRuleEntityService.selectBySVRuleInfos(contextOwner.getAccountId(), fmpTop5SvRuleInfos);            //这个list理应包含所有SVRuleInfo的所有实体相关数据
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
            if(calculationLogType != Constant.NO_CALCULATION_LOG){
                calculationLogParam_fpm.setRuleId_RRuleEntityDataMap(ruleId_RRuleEntityDataMap);
            }

            //3.计算相似度，并选择最优集合
            List<SVRuleInfo> svRuleInfosResult = similarityCalculateService.similarityCalculate_FPM(algorithmType, fmpTop5SvRuleInfos, ruleId_RRuleEntityDataMap, calculationLogParam_fpm); //svRuleInfosResult不可能为null，且传进去多少，也返回多少
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
            if(svRuleInfosResult != null && svRuleInfosResult.size() > 0){
                if(calculationLogType != Constant.NO_CALCULATION_LOG){
                    componentConstant.putDataComponent(new DataComponentBase("FPM_MatchResults", contextOwner, "List<SVRuleInfo>", svRuleInfosResult));     //埋点3：全参匹配初选结果集埋点，能够知道全参匹配初选过程选定了哪些rule
                }
                SVRuleInfo optimalSvRuleInfo_FPM = svRuleInfosResult.get(0);                                                        //获取相似度值最大的那一个（最优结果）
                optimalSvRuleInfo_FPM.setMatchType(Constant.FPM);                                                                   //设置匹配类型
                optimalSvRuleInfo_FPM.setAlgorithmType(algorithmType);                                                              //设置算法类型
                optimalSvRuleInfo_FPM.setrEntityWordInfosList(null);                                                                //最优规则找到后，需要保存（本地、远程），此过程数据太大而且后续没有更多作用，故此处设置为null
                return new ComponentBizResult("FPMC_S", Constant.STORE_LOCAL, optimalSvRuleInfo_FPM);      //此时不计较全参匹配结果是否超过阈值，此结果在本地存储
            }
        }
        return new ComponentBizResult("FPMC_F", "FPMC_F_NoMatchRule");      //此结果在本地和远程都要存储
    }
}
