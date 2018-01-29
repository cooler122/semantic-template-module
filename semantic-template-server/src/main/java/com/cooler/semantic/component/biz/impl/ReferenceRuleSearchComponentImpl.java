package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SVRuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("referenceRuleSearchComponent")
public class ReferenceRuleSearchComponentImpl extends FunctionComponentBase<SVRuleInfo, Object> {

    private static Logger logger = LoggerFactory.getLogger(ReferenceRuleSearchComponentImpl.class.getName());

    public ReferenceRuleSearchComponentImpl() {
        super("RRSC", "optimalSvRuleInfo", null);
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, SVRuleInfo svRuleInfo) {
        logger.trace("RRSC.指代规则检索");

        System.out.println(JSON.toJSONString(svRuleInfo));

        return new ComponentBizResult("RRSC_S");
    }
}
