package com.cooler.semantic.facade;

import com.cooler.semantic.api.SemanticParserFacade;
import com.cooler.semantic.component.ComponentConstant;
import com.cooler.semantic.component.biz.SemanticComponent;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.model.ContextOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@org.springframework.stereotype.Component("semanticParserFacade")
public class SemanticParserFacadeImpl implements SemanticParserFacade {

    private static Logger logger = LoggerFactory.getLogger(SemanticParserFacadeImpl.class.getName());

    @Autowired
    @Qualifier("validateConfComponent")
    private SemanticComponent startComponent;                                                                       //1.设定起始点，初始功能组件（直接加载）

    @Autowired
    private ComponentConstant componentConstant;

    public SemanticParserResponse semanticParse(SemanticParserRequest request) {
        logger.info("开始解析...");

        //2.构建用户上下文坐标
        List<Integer> accountIds = request.getAccountIds();
        Integer coreAccountId = accountIds.get(0);                                                                     //用户账号
        Integer userId = request.getUserId();                                                                          //用户编号
        Integer contextId = request.getContextId();                                                                    //上下文编号
        ContextOwner contextOwner = new ContextOwner(coreAccountId, userId, contextId);                                //上下文拥有者对象

        //2.加载数据
        DataComponent<SemanticParserRequest> dataComponent = new DataComponentBase<>("semanticParserRequest", contextOwner, "SemanticParserRequest", request);     //初始用户的瞬时数据到数据组件
        componentConstant.putDataComponent(dataComponent);                                                          //加载初始数据组件

        startComponent.functionRun(contextOwner);                                                                    //执行

        String traceByContextOwnerIndex = componentConstant.getTraceByContextOwnerIndex(contextOwner.getOwnerIndex());
        System.out.println(traceByContextOwnerIndex);

        return null;
    }
}
