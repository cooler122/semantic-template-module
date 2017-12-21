package com.cooler.semantic.facade;

import com.cooler.semantic.api.SemanticParserFacade;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.biz.FunctionComponent;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("semanticParserFacade")
public class SemanticParserFacadeImpl implements SemanticParserFacade {

    private static Logger logger = LoggerFactory.getLogger(SemanticParserFacadeImpl.class.getName());

    @Autowired
    @Qualifier("validateConfComponent")
    private FunctionComponent startComponent;                                                                       //初始功能组件（直接加载）

    @Autowired
    private ComponentConstant componentConstant;

    public SemanticParserResponse semanticParse(SemanticParserRequest request) {
        logger.info("开始解析...");

        DataComponent<SemanticParserRequest> dataComponent = new DataComponentBase<>("semanticParserRequest","SemanticParserRequest", request);     //初始数据组件
        componentConstant.getDataBeanMap().put("semanticParserRequest", dataComponent);                         //加载初始数据组件

        startComponent.run();                                                                                       //执行

        return null;
    }
}
