package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.springframework.stereotype.Component;

@Component("resultPack4RunningComponent")
public class ResultPack4RunningComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {

    public ResultPack4RunningComponentImpl() {
        super("RPC4R", "SO-13", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        String sentence = svRuleInfo.getSentence();
        Double similarity = svRuleInfo.getSimilarity();
        long responseTimestamp = System.currentTimeMillis();

        SemanticParserResponse semanticParserResponse = new SemanticParserResponse();
        semanticParserResponse.setSentence(sentence);
        semanticParserResponse.setResponseType(1);
        semanticParserResponse.setScore(similarity);
        semanticParserResponse.setResponseTimestamp(responseTimestamp);
        semanticParserResponse.setState(1);

        return new ComponentBizResult("RPC4R_S", Constant.STORE_LOCAL, semanticParserResponse);
    }
}
