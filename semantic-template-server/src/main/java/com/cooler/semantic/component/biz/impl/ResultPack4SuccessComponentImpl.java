package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.springframework.stereotype.Component;

@Component("resultPack4SuccessComponent")
public class ResultPack4SuccessComponentImpl extends FunctionComponentBase<SVRuleInfo, SemanticParserResponse> {

    public ResultPack4SuccessComponentImpl() {
        super("RPC4S", "SO-13", "optimalSvRuleInfo", "semanticParserResponse");
    }

    @Override
    protected ComponentBizResult<SemanticParserResponse> runBiz(ContextOwner contextOwner, SVRuleInfo bizData) {

        return new ComponentBizResult("RPC4S");
    }
}
