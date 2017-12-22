package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.biz.ENDComponent;
import com.cooler.semantic.component.ComponentBizResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("eNDComponent")
public class ENDComponentImpl extends FunctionComponentBase<Object, Object> implements ENDComponent {

    private static Logger logger = LoggerFactory.getLogger(ENDComponentImpl.class.getName());

    public ENDComponentImpl() {
        super("ENDC", "END", null, null);
    }

    @Override
    protected ComponentBizResult<Object> runBiz(Object bizData) {
        logger.info("END.结束流程");

        //TODO:校验和配置逻辑

        return new ComponentBizResult("END", false, null);
    }
}
