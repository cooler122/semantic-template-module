package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component("resultPack4RunningComponent")
public class ResultPack4RunningComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {
    private static Logger logger = LoggerFactory.getLogger(ResultPack4RunningComponentImpl.class.getName());
    public ResultPack4RunningComponentImpl() {
        super("RPC4R", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("RPC4R.运行中意图结果包装");
        String sentence = svRuleInfo.getSentence();
        Double similarity = svRuleInfo.getSimilarity();
        similarity = new BigDecimal(similarity).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        long responseTimestamp = System.currentTimeMillis();
        String sentenceModified = StringUtils.collectionToDelimitedString(svRuleInfo.getWords(), "");
        svRuleInfo.setSentenceModified(sentenceModified);

        SemanticParserResponse semanticParserResponse = new SemanticParserResponse();
        semanticParserResponse.setSentence(sentence);
        semanticParserResponse.setSentenceModified(sentenceModified);
        semanticParserResponse.setResponseType(Constant.RUNNING_RESULT);
        semanticParserResponse.setScore(similarity);
        semanticParserResponse.setResponseTimestamp(responseTimestamp);
        semanticParserResponse.setState(1);

        return new ComponentBizResult("RPC4R_S", Constant.STORE_LOCAL, semanticParserResponse);
    }
}
