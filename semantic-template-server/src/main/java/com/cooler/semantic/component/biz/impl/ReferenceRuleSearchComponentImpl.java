package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.*;
import com.cooler.semantic.model.*;
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
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, SVRuleInfo optimalSvRuleInfo) {
        logger.trace("RRSC.指代规则检索");

        DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = dataComponent.getData();
        String sentence = request.getCmd();
        int algorithmType = request.getAlgorithmType();
        int contextLogType = request.getContextLogType();                                                               //上下文日志类型

        Integer accountId = optimalSvRuleInfo.getAccountId();
        Integer ruleId = optimalSvRuleInfo.getRuleId();
        Integer guideIntentId = optimalSvRuleInfo.getGuideIntentId();
        if(guideIntentId == null){                                                                                     //进入引导规则之前，SVRuleInfo是没有引导意图的，第一次确定引导意图ID
            guideIntentId = ruleService.selectIntentId(ruleId);
            optimalSvRuleInfo.setGuideIntentId(guideIntentId);
        }

        Map<Integer, String> matchedREWIMap = new HashMap<>();
        List<REntityWordInfo> matchedREntityWordInfos = optimalSvRuleInfo.getMatchedREntityWordInfos();
        for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
            matchedREWIMap.put(matchedREntityWordInfo.getEntityId(), matchedREntityWordInfo.getWord());
        }

        List<Integer> enableReferRuleIds = new ArrayList<>();
        List<Integer> disableReferRuleIds = new ArrayList<>();

        List<ReferRuleRelation> referRuleRelations = referRuleRelationService.selectByRIdAId(ruleId, guideIntentId, accountId);
        for(ReferRuleRelation referRuleRelation : referRuleRelations){
            Integer referRuleId = referRuleRelation.getReferRuleId();
            Byte hasConditions = referRuleRelation.getHasConditions();
            if(hasConditions == (byte)0){                                                                               //如果为0，代表没有条件
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

        enableReferRuleIds.removeAll(disableReferRuleIds);                                                              //enableReferRuleIds里面很可能包含disableReferRuleIds的元素
        int enableReferRuleIdsSize = enableReferRuleIds.size();
        if(enableReferRuleIdsSize > 0){
            if(enableReferRuleIdsSize > 1){
                String ruleName = optimalSvRuleInfo.getRuleName();
                logger.warn("流程设计错误！意图id为：" + guideIntentId + "，规则id为： " + ruleId + " (" + ruleName + ")的规则，REWI为" + JSON.toJSONString(matchedREntityWordInfos) + ", 获得的指向规则数量大于1，指向规则ID集为：" + Arrays.toString(enableReferRuleIds.toArray()));
            }

            List<List<REntityWordInfo>> currentREntityWordInfosList = optimalSvRuleInfo.getrEntityWordInfosList();      //进入被引导规则集的首个规则，所得到的REWI是不确定的，要全部放入Map中
            Map<String, REntityWordInfo> fixedAccumulatedMatchedREWIMap = optimalSvRuleInfo.getFixedAccumulatedMatchedREWIMap();    //确定的REWI实体集合（第一次设值好所有必须实体的槽位，没值的设null）
            if(fixedAccumulatedMatchedREWIMap == null){                                                                //如果此Map为null，说明是第一次由引导规则进入被引导规则集合的时刻
                Map<String, List<REntityWordInfo>> accumulatedMatchedREWIsMap = new HashMap<>();                            //新建这个Map，装载本次长对话的REWIs的Map
                for (List<REntityWordInfo> rEntityWordInfos : currentREntityWordInfosList) {
                    for (REntityWordInfo rEntityWordInfo : rEntityWordInfos) {
                        String entityTypeId = rEntityWordInfo.getEntityTypeId();
                        List<REntityWordInfo> rEntityWordInfosTmp = accumulatedMatchedREWIsMap.get(entityTypeId);           //本轮长对话可能不止一个entityTypeId的实体，故要弄一个List
                        if(rEntityWordInfosTmp == null)  rEntityWordInfosTmp = new ArrayList<>();
                        rEntityWordInfosTmp.add(rEntityWordInfo);
                        accumulatedMatchedREWIsMap.put(entityTypeId, rEntityWordInfosTmp);
                    }
                }
                List<Integer> referRuleIds = referRuleRelationService.selectRRIdsByIntentId(accountId, guideIntentId);
                List<RRuleEntity> allNeededRREs = rRuleEntityService.selectByRuleIds(accountId, referRuleIds);       //TODO：这一步实际上应该放到ES里面去做
                fixedAccumulatedMatchedREWIMap = new HashMap<>();
                for (RRuleEntity neededRRE : allNeededRREs) {
                    String entityTypeId = neededRRE.getEntityTypeId();
                    List<REntityWordInfo> rEntityWordInfos = accumulatedMatchedREWIsMap.get(entityTypeId);
                    if(rEntityWordInfos != null && rEntityWordInfos.size() == 1){                                      //如果Map中有且只有一个值，那么就认定这个值是被引导规则中空缺实体所需要的值了
                        fixedAccumulatedMatchedREWIMap.put(entityTypeId, rEntityWordInfos.get(0));
                    }else if(rEntityWordInfos != null && rEntityWordInfos.size() > 1){                                //如果map中一个实体的值不只一个，不能确认是哪一个，则干脆删掉（留一个槽位给此实体，设值为null）
                        accumulatedMatchedREWIsMap.remove(entityTypeId);
                        fixedAccumulatedMatchedREWIMap.put(entityTypeId, null);
                    }else if(rEntityWordInfos == null){                                                               //将没收集到的必须实体留一个槽位，设值null
                        fixedAccumulatedMatchedREWIMap.put(entityTypeId, null);
                    }
                }
            }else{                                                                                                     //fixedAccumulatedMatchedREWIMap里面已经有值了，那么后面就直接塞入每轮对话产生的值
                for (List<REntityWordInfo> rEntityWordInfos : currentREntityWordInfosList) {
                    for (REntityWordInfo rEntityWordInfo : rEntityWordInfos) {
                        String entityTypeId = rEntityWordInfo.getEntityTypeId();
                        fixedAccumulatedMatchedREWIMap.put(entityTypeId, rEntityWordInfo);
                    }
                }
            }

            Integer enableReferRuleId = enableReferRuleIds.get(0);                                                      //需要指向的rule的ID编号，如果数量大于1，那么只能取第一个了
            Rule referRule = ruleService.selectByPrimaryKey(enableReferRuleId);                                       //获取本轮的指代规则
            List<RRuleEntity> lackRRuleEntities = rRuleEntityService.selectNecessaryByAIdRId(accountId, enableReferRuleId); //获取指代规则的必须实体，用来一个个去问，从这里对话主动权转给了机器 //TODO：这一步也应该放到ES里面去做
            StringBuffer baseMatchSentenceSB = new StringBuffer();
            for (RRuleEntity lackRRuleEntity : lackRRuleEntities) {
                String entityTypeId = lackRRuleEntity.getEntityTypeId();
                REntityWordInfo rEntityWordInfo = fixedAccumulatedMatchedREWIMap.get(entityTypeId);
                if(rEntityWordInfo != null){
                    baseMatchSentenceSB.append(rEntityWordInfo.getWord()).append(",");
                }
            }

            String ruleName = referRule.getRuleName();

            String baseMatchSentence = baseMatchSentenceSB.append(referRule.getBaseMatchSentence()).toString();
//            Integer accountId = rule.getAccountId();
            Double accuracyThreshold = referRule.getAccuracyThreshold();
            accuracyThreshold = accuracyThreshold != -1d ? accuracyThreshold : request.getAccuracyThreshold();
            String[] baseMatchWords = baseMatchSentence.split(",");

            SVRuleInfo createdReferSvRuleInfo = new SVRuleInfo();
            createdReferSvRuleInfo.setIsLongConversationRule(true);
            createdReferSvRuleInfo.setAccountId(accountId);
            createdReferSvRuleInfo.setSentenceVectorId(0);
            createdReferSvRuleInfo.setSentence(sentence);
            createdReferSvRuleInfo.setSentenceModified(sentence);
            createdReferSvRuleInfo.setReferRuleInitSentence(baseMatchSentence);
            createdReferSvRuleInfo.setWords(Arrays.asList(baseMatchWords));

            createdReferSvRuleInfo.setGuideIntentId(guideIntentId);                                                     //上次的引导意图ID也是本次的引导意图ID
            createdReferSvRuleInfo.setRuleId(enableReferRuleId);
            createdReferSvRuleInfo.setRuleName(ruleName);
            createdReferSvRuleInfo.setAlgorithmType(algorithmType);
            createdReferSvRuleInfo.setSimilarity(accuracyThreshold + 0.01d);
            createdReferSvRuleInfo.setRunningAccuracyThreshold(accuracyThreshold);
//            createdReferSvRuleInfo.setRedundantWordInfos(new ArrayList<WordInfo>());
            createdReferSvRuleInfo.setMatchedREntityWordInfos(new ArrayList<REntityWordInfo>());
            createdReferSvRuleInfo.setMatchedRRuleEntities(new ArrayList<RRuleEntity>());
            createdReferSvRuleInfo.setLackedRRuleEntities(lackRRuleEntities);
            createdReferSvRuleInfo.setFixedAccumulatedMatchedREWIMap(fixedAccumulatedMatchedREWIMap);

            return new ComponentBizResult("RRSC_S", contextLogType == 0 ? Constant.STORE_LOCAL_REMOTE : Constant.STORE_LOCAL_REMOTE_CONTEXTLOG, createdReferSvRuleInfo);
        }else{
            return new ComponentBizResult("RRSC_F");                                                          //如果前面进入这个分支，表示有指向性规则，但各个条件的限制导致没有合适的的指向性规则，那么要返回匹配失败结果体了。
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
