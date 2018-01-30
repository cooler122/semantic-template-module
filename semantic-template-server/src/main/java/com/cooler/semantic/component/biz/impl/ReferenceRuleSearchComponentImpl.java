package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.entity.ReferRuleCondition;
import com.cooler.semantic.entity.ReferRuleRelation;
import com.cooler.semantic.entity.Rule;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.ReferRuleConditionService;
import com.cooler.semantic.service.external.ReferRuleRelationService;
import com.cooler.semantic.service.internal.RuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("referenceRuleSearchComponent")
public class ReferenceRuleSearchComponentImpl extends FunctionComponentBase<SVRuleInfo, Object> {

    private static Logger logger = LoggerFactory.getLogger(ReferenceRuleSearchComponentImpl.class.getName());

    @Autowired
    private RuleService ruleService;
    @Autowired
    private ReferRuleRelationService referRuleRelationService;
    @Autowired
    private ReferRuleConditionService referRuleConditionService;

    public ReferenceRuleSearchComponentImpl() {
        super("RRSC", "optimalSvRuleInfo", null);
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("RRSC.指代规则检索");

        DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = dataComponent.getData();

        Integer ruleId = svRuleInfo.getRuleId();
        Map<Integer, String> matchedREWIMap = new HashMap<>();
        List<REntityWordInfo> matchedREntityWordInfos = svRuleInfo.getMatchedREntityWordInfos();
        for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
            matchedREWIMap.put(matchedREntityWordInfo.getEntityId(), matchedREntityWordInfo.getWord());
        }

        List<Integer> enableReferRuleIds = new ArrayList<>();
        List<Integer> disableReferRuleIds = new ArrayList<>();

        List<ReferRuleRelation> referRuleRelations = referRuleRelationService.selectByRuleId(ruleId);
        for (ReferRuleRelation referRuleRelation : referRuleRelations) {
            Integer referRuleId = referRuleRelation.getReferRuleId();
            Byte hasConditions = referRuleRelation.getHasConditions();
            if(hasConditions == (byte)0){                                                                              //如果为0，代表没有条件
                enableReferRuleIds.add(referRuleId);
            } else {                                                                                                    //如果不为0，代表有条件
                Integer referRuleRelationId = referRuleRelation.getId();
                List<ReferRuleCondition> referRuleConditions = referRuleConditionService.selectByRRRelationId(referRuleRelationId);
                if(referRuleConditions != null && referRuleConditions.size() > 0){
                    for (ReferRuleCondition referRuleCondition : referRuleConditions) {
                        Integer requiredEntityType = referRuleCondition.getEntityType();
                        Integer requiredEntityId = referRuleCondition.getEntityId();
                        Integer logicExpression = referRuleCondition.getLogicExpression();
                        String referenceEntityParam = referRuleCondition.getEntityParam();                              //参考值（或集合）

                        String matchedEntityParam = matchedREWIMap.get(requiredEntityId);
                        boolean isSatisfied = compareReferenceEntityParam(requiredEntityType, requiredEntityId, matchedEntityParam, logicExpression, referenceEntityParam);
                        if(isSatisfied){
                            enableReferRuleIds.add(referRuleId);
                        }else{
                            disableReferRuleIds.add(referRuleId);
                        }
                    }
                }
            }
        }
        enableReferRuleIds.removeAll(disableReferRuleIds);
        if(enableReferRuleIds != null && enableReferRuleIds.size() > 0){
            if(enableReferRuleIds.size() > 1){
                String ruleName = svRuleInfo.getRuleName();
                logger.warn("规则id为： " + ruleId + " （" + ruleName + ")的规则，REWI为" + JSON.toJSONString(matchedREntityWordInfos) + ", 获得的指向规则数量大于1，指向规则ID集为：" + Arrays.toString(enableReferRuleIds.toArray()));
            }
            Integer enableReferRuleId = enableReferRuleIds.get(0);                                                      //需要指向的rule的ID编号
            Rule rule = ruleService.selectByPrimaryKey(enableReferRuleId);

            String baseMatchSentence = rule.getBaseMatchSentence();
            Integer accountId = rule.getAccountId();
            Double accuracyThreshold = rule.getAccuracyThreshold();
            accuracyThreshold = accuracyThreshold != -1d ? accuracyThreshold : request.getAccuracyThreshold();
            String[] baseMatchWords = baseMatchSentence.split(",");

            SVRuleInfo referOptimalSvRuleInfo = new SVRuleInfo();
            referOptimalSvRuleInfo.setAccountId(accountId);
            referOptimalSvRuleInfo.setSimilarity(accuracyThreshold + 0.01d);
            referOptimalSvRuleInfo.setRunningAccuracyThreshold(accuracyThreshold);
            referOptimalSvRuleInfo.setSentence(baseMatchSentence);
            referOptimalSvRuleInfo.setWords(Arrays.asList(baseMatchWords));
            //TODO:可能rule要分成简单rule和指代性rule了，重新设置一个字段来区分这两类：简单rule是直接产生回应的rule，指代性rule是指向另一个rule的rule，指代性rule要有预置语句，让其期初匹配超过阈值
            //TODO:可能要规划一条线，专门走指代rule，即碰到指代规则进行了句子实体归属后就不进入3类匹配流程了，而专门设置一条匹配路线，直接计算指代规则和新的SV的权重，然后到ORSC模块，还要设计一个对话管理模块，专门来管理多条线路的对话。


        }else{

        }

        return new ComponentBizResult("RRSC_S");
    }

    private boolean compareReferenceEntityParam(Integer requiredEntityType, Integer requiredEntityId, String matchedEntityParam, Integer logicExpression, String referenceEntityParam) {
        //TODO:根据不同类别的entityType，来对比matchedEntityParam和referenceEntityParam的值，看看谁更大，或是否满足logicExpression的关系
//        if(logicExpression == 0){
//            if(matchedEntityParam == entityParam) {
//                System.out.println("record it");
//            }
//        }else if(logicExpression > 0){
//            if(matchedEntityParam > entityParam) {
//                System.out.println("record it");
//            }
//        }else{
//
//        }
        return false;
    }
}
