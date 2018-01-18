package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.internal.AccountConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component("resultPack4MissingComponent")
public class ResultPack4MissingComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {

    @Autowired
    private AccountConfigurationService accountConfigurationService;

    public ResultPack4MissingComponentImpl() {
        super("RPC4M", "SO-16", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        String sentence = svRuleInfo.getSentence();
        Double similarity = svRuleInfo.getSimilarity();
        List<RRuleEntity> lackedRRuleEntities = svRuleInfo.getLackedRRuleEntities();
        int lackedSize = lackedRRuleEntities.size();
        String sentenceModified = StringUtils.collectionToDelimitedString(svRuleInfo.getWords(), "");
        svRuleInfo.setSentenceModified(sentenceModified);

        String responseMsg = "您问的是 ";
        if(lackedRRuleEntities != null && lackedSize > 1){                                                             //1.如果缺失实体问题数量 > 1
            Integer accountId = contextOwner.getAccountId();
            Integer userId = contextOwner.getUserId();
            AccountConfiguration accountConfiguration = accountConfigurationService.selectAIdUId(accountId, userId);
            Boolean canBatchQuery = accountConfiguration.getCanBatchQuery();                                            //查询缺参状态下是否可以批量询问
            if(canBatchQuery){                                                                                          //如果缺参问题可以批量询问
                for (RRuleEntity lackedRRuleEntity : lackedRRuleEntities) {
                    responseMsg += lackedRRuleEntity.getNecessaryEntityQuery() + "\t";
                }
            }else{                                                                                                      //如果缺参问题不可以批量询问，则先排序，后取最前的一个缺失实体的问题进行询问
                Collections.sort(lackedRRuleEntities, new Comparator<RRuleEntity>() {
                    @Override
                    public int compare(RRuleEntity o1, RRuleEntity o2) {
                        Integer entityId1 = o1.getEntityId();
                        Integer entityId2 = o2.getEntityId();
                        Double weight1 = o1.getWeight();
                        Double weight2 = o2.getWeight();
                        if (weight1 > weight2) {
                            return -1;
                        } else if (weight1 < weight2) {
                            return 1;
                        } else {
                            if (entityId1 > entityId2) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }
                });
                RRuleEntity rRuleEntity = lackedRRuleEntities.get(0);
                responseMsg = rRuleEntity.getNecessaryEntityQuery();
            }
        }else{                                                                                                         //2.如果缺失实体问题数量 = 1
            RRuleEntity rRuleEntity = lackedRRuleEntities.get(0);
            responseMsg = rRuleEntity.getNecessaryEntityQuery();
        }


        long responseTimestamp = System.currentTimeMillis();

        SemanticParserResponse semanticParserResponse = new SemanticParserResponse();
        semanticParserResponse.setResponseMsg(responseMsg);
        semanticParserResponse.setSentence(sentence);
        semanticParserResponse.setSentenceModified(sentenceModified);
        semanticParserResponse.setResponseType(-1 * lackedSize);
        semanticParserResponse.setScore(similarity);
        semanticParserResponse.setResponseTimestamp(responseTimestamp);
        semanticParserResponse.setState(-1 * lackedSize);

        return new ComponentBizResult("RPC4M_S", Constant.STORE_LOCAL, semanticParserResponse);
    }
}
