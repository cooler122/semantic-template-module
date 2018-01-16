package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.model.ContextOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("eNDComponent")
public class ENDComponentImpl extends FunctionComponentBase<Object, Object> {

    private static Logger logger = LoggerFactory.getLogger(ENDComponentImpl.class.getName());

    public ENDComponentImpl() {
        super("ENDC", "END", null, null);
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, Object bizData) {

        //TODO:校验和配置逻辑

        return new ComponentBizResult("END");
    }
}
