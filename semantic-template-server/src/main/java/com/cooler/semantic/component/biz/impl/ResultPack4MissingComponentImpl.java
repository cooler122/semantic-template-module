package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component("resultPack4MissingComponent")
public class ResultPack4MissingComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {
    private static Logger logger = LoggerFactory.getLogger(ResultPack4MissingComponentImpl.class.getName());

    public ResultPack4MissingComponentImpl() {
        super("RPC4M", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("RPC4M.缺参结果包装");
        String sentence = svRuleInfo.getSentence();
        Double similarity = svRuleInfo.getSimilarity();
        similarity = new BigDecimal(similarity).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<RRuleEntity> lackedRRuleEntities = svRuleInfo.getLackedRRuleEntities();
        int lackedSize = lackedRRuleEntities.size();
        String sentenceModified = StringUtils.collectionToDelimitedString(svRuleInfo.getWords(), "");
        svRuleInfo.setSentenceModified(sentenceModified);
        String responseMsg = "";

        DataComponent sugguestDataDataComponent = componentConstant.getDataComponent("sugguestData", contextOwner);         //看看前面是否获取了建议语句
        if(sugguestDataDataComponent != null && sugguestDataDataComponent.getData() != null){
            responseMsg = sugguestDataDataComponent.getData() + "，前面您问的是：";
        }

        if(lackedRRuleEntities != null && lackedSize > 1){                                                             //1.如果缺失实体问题数量 > 1
            DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
            SemanticParserRequest request = dataComponent.getData();
            Boolean canBatchQuery = request.isCanBatchQuery();                                                          //查询缺参状态下是否可以批量询问
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
                responseMsg += rRuleEntity.getNecessaryEntityQuery();
            }
        }else{                                                                                                         //2.如果缺失实体问题数量 = 1
            RRuleEntity rRuleEntity = lackedRRuleEntities.get(0);
            responseMsg += rRuleEntity.getNecessaryEntityQuery();
        }


        long responseTimestamp = System.currentTimeMillis();

        SemanticParserResponse semanticParserResponse = new SemanticParserResponse();
        semanticParserResponse.setResponseMsg(responseMsg);
        semanticParserResponse.setSentence(sentence);
        semanticParserResponse.setSentenceModified(sentenceModified);
        semanticParserResponse.setResponseType(Constant.MISSING_RESULT);
        semanticParserResponse.setScore(similarity);
        semanticParserResponse.setResponseTimestamp(responseTimestamp);
        semanticParserResponse.setState(-1 * lackedSize);

        return new ComponentBizResult("RPC4M_S", Constant.STORE_LOCAL, semanticParserResponse);
    }
}
