package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Component("resultPack4NoRunningComponent")
public class ResultPack4NoRunningComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {

    public ResultPack4NoRunningComponentImpl() {
        super("RPC4NR", "SO-13", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        String sentence = svRuleInfo.getSentence();
        Double similarity = svRuleInfo.getSimilarity();
        long responseTimestamp = System.currentTimeMillis();
        String sentenceModified = StringUtils.collectionToDelimitedString(svRuleInfo.getWords(), "");
        svRuleInfo.setSentenceModified(sentenceModified);

        SemanticParserResponse semanticParserResponse = new SemanticParserResponse();
        semanticParserResponse.setSentence(sentence);
        semanticParserResponse.setSentenceModified(sentenceModified);
        semanticParserResponse.setResponseType(0);
        semanticParserResponse.setScore(similarity);
        semanticParserResponse.setResponseTimestamp(responseTimestamp);
        semanticParserResponse.setState(0);
        return new ComponentBizResult("RPC4NR_S", Constant.STORE_LOCAL, semanticParserResponse);
    }
}
