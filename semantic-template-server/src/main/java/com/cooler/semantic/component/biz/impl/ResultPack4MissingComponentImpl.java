package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component("resultPack4MissingComponent")
public class ResultPack4MissingComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {

    public ResultPack4MissingComponentImpl() {
        super("RPC4M", "SO-13", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        String sentence = svRuleInfo.getSentence();
        Double similarity = svRuleInfo.getSimilarity();
        List<RRuleEntity> lackedRRuleEntities = svRuleInfo.getLackedRRuleEntities();
        int lackedSize = lackedRRuleEntities.size();
        String responseMsg = "抱歉，还有参数未明确！";
        if(lackedRRuleEntities != null && lackedSize > 0){
            Collections.sort(lackedRRuleEntities, new Comparator<RRuleEntity>() {                                           //TODO:如果用户设置中，设置一个个的补参，那么需要在这里排序，如果不需要，则
                @Override
                public int compare(RRuleEntity o1, RRuleEntity o2) {
                    Integer entityId1 = o1.getEntityId();
                    Integer entityId2 = o2.getEntityId();
                    Double weight1 = o1.getWeight();
                    Double weight2 = o2.getWeight();
                    if(weight1 > weight2){
                        return -1;
                    }else if(weight1 < weight2){
                        return 1;
                    }else{
                        if(entityId1 > entityId2){
                            return -1;
                        }else{
                            return 1;
                        }
                    }
                }
            });

            RRuleEntity rRuleEntity = lackedRRuleEntities.get(0);
            responseMsg = rRuleEntity.getNecessaryEntityQuery();
        }

        long responseTimestamp = System.currentTimeMillis();

        SemanticParserResponse semanticParserResponse = new SemanticParserResponse();
        semanticParserResponse.setResponseMsg(responseMsg);
        semanticParserResponse.setSentence(sentence);
        semanticParserResponse.setResponseType(-1 * lackedSize);
        semanticParserResponse.setScore(similarity);
        semanticParserResponse.setResponseTimestamp(responseTimestamp);
        semanticParserResponse.setState(-1 * lackedSize);

        return new ComponentBizResult("RPC4M_S", Constant.STORE_LOCAL, semanticParserResponse);
    }
}
