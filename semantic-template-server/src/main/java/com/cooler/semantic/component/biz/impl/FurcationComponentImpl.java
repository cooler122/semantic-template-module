package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("furcationComponentImpl")
public class FurcationComponentImpl extends FunctionComponentBase<Object, Object> {

    private static Logger logger = LoggerFactory.getLogger(FurcationComponentImpl.class.getName());

    public FurcationComponentImpl() {
        super("FC", "D1", null, null);
    }

    @Override
    protected ComponentBizResult<Object> runBiz(Object bizData) {
        logger.info("SO_1.校验和配置");

        return new ComponentBizResult("VCC_S", true, bizData);
    }

}
