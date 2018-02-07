package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.*;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.MatchedEntityParam;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.ReferRuleConditionService;
import com.cooler.semantic.service.external.ReferRuleRelationService;
import com.cooler.semantic.service.internal.RRuleEntityService;
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
    private RRuleEntityService rRuleEntityService;
    @Autowired
    private ReferRuleRelationService referRuleRelationService;
    @Autowired
    private ReferRuleConditionService referRuleConditionService;

    public ReferenceRuleSearchComponentImpl() {
        super("RRSC", "optimalSvRuleInfo", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("RRSC.指代规则检索");

        DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = dataComponent.getData();
        String sentence = request.getCmd();
        int algorithmType = request.getAlgorithmType();

        Integer ruleId = svRuleInfo.getRuleId();
        List<RRuleEntity> matchedRRuleEntities = svRuleInfo.getMatchedRRuleEntities();
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
                logger.warn("规则id为： " + ruleId + " (" + ruleName + ")的规则，REWI为" + JSON.toJSONString(matchedREntityWordInfos) + ", 获得的指向规则数量大于1，指向规则ID集为：" + Arrays.toString(enableReferRuleIds.toArray()));
            }
            Integer enableReferRuleId = enableReferRuleIds.get(0);                                                      //需要指向的rule的ID编号
            Rule rule = ruleService.selectByPrimaryKey(enableReferRuleId);
            List<RRuleEntity> lackRRuleEntities = rRuleEntityService.selectNecessaryByAIdRId(contextOwner.getAccountId(), enableReferRuleId);

            String ruleName = rule.getRuleName();
            String baseMatchSentence = rule.getBaseMatchSentence();
            Integer accountId = rule.getAccountId();
            Double accuracyThreshold = rule.getAccuracyThreshold();
            accuracyThreshold = accuracyThreshold != -1d ? accuracyThreshold : request.getAccuracyThreshold();
            String[] baseMatchWords = baseMatchSentence.split(",");

            SVRuleInfo createdReferSvRuleInfo = new SVRuleInfo();
            createdReferSvRuleInfo.setLongConversationRule(true);
            createdReferSvRuleInfo.setAccountId(accountId);
            createdReferSvRuleInfo.setSentenceVectorId(0);
            createdReferSvRuleInfo.setSentence(sentence);
            createdReferSvRuleInfo.setSentenceModified(sentence);
            createdReferSvRuleInfo.setReferRuleInitSentence(baseMatchSentence);
            createdReferSvRuleInfo.setWords(Arrays.asList(baseMatchWords));

            createdReferSvRuleInfo.setRuleId(enableReferRuleId);
            createdReferSvRuleInfo.setRuleName(ruleName);
            createdReferSvRuleInfo.setAlgorithmType(algorithmType);
            createdReferSvRuleInfo.setSimilarity(accuracyThreshold + 0.01d);
            createdReferSvRuleInfo.setRunningAccuracyThreshold(accuracyThreshold);
            createdReferSvRuleInfo.setMatchedREntityWordInfos(matchedREntityWordInfos);                                 //注意这是直接rule匹配下的REWI集合
            createdReferSvRuleInfo.setMatchedRRuleEntities(matchedRRuleEntities);
            createdReferSvRuleInfo.setLackedRRuleEntities(lackRRuleEntities);                                           //注意这是指代rule匹配下的RRE集合

            if(matchedRRuleEntities != null && matchedRRuleEntities.size() > 0){
                List<MatchedEntityParam> matchedEntityParams = new ArrayList<>();
                for (RRuleEntity matchedRRuleEntity : matchedRRuleEntities) {
                    Integer entityType = matchedRRuleEntity.getEntityType();
                    Integer entityId = matchedRRuleEntity.getEntityId();
                    String entityName = matchedRRuleEntity.getEntityName();
                    String entityAppName = matchedRRuleEntity.getEntityAppName();
                    String value = matchedREWIMap.get(entityId);                                                        //获取这个匹配上的值
                    MatchedEntityParam matchedEntityParam = new MatchedEntityParam(ruleId, entityType, entityId, entityName, entityAppName, value);
                    matchedEntityParams.add(matchedEntityParam);
                }
                createdReferSvRuleInfo.setAccumulatedMatchedEntityParams(matchedEntityParams);                          //设置各个指向性规则积累的值
            }

            return new ComponentBizResult("RRSC_S", Constant.STORE_LOCAL_REMOTE, createdReferSvRuleInfo);
        }else{

            return new ComponentBizResult("RRSC_F");                                                     //如果前面进入这个分支，表示有指向性规则，但各个条件的限制导致没有合适的的指向性规则，那么要返回匹配失败结果体了。
        }
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
