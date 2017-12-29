package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.model.ContextOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("contextLPDataSearchComponent")
public class ContextLPDataSearchComponentImpl extends FunctionComponentBase<Object, Object> {

    private static Logger logger = LoggerFactory.getLogger(ContextLPDataSearchComponentImpl.class.getName());

    public ContextLPDataSearchComponentImpl() {
        super("CLPDSC", "SO_4", null, "contextLPDataBean");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, Object bizData) {
        logger.info("SO_1.校验和配置");

        return new ComponentBizResult("VCC_S",1, bizData);
    }

}
