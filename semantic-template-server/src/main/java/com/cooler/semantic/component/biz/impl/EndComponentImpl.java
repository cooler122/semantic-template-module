package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.service.external.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("endComponent")
public class EndComponentImpl extends FunctionComponentBase<Object, Object> {

    private static Logger logger = LoggerFactory.getLogger(EndComponentImpl.class.getName());
    @Autowired
    private LogService logService;

    public EndComponentImpl() {
        super("ENDC", null, null);
    }

    @Override
    protected ComponentBizResult<Object> runBiz(final ContextOwner contextOwner, Object bizData) {
        logger.trace("结束！");

        Thread thread = new Thread(){
            @Override
            public void run() {
                //1.准备日志信息      //TODO：(此处如果异步处理，可能到了起始的删除日志的时候，这个还没有写进去，但一般还是比较少见这种情况的）
                DataComponent<SemanticParserRequest> requestDataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
                SemanticParserRequest request = requestDataComponent.getData();
                String processTrace = componentConstant.getTraceByContextOwnerIndex(contextOwner.getOwnerIndex());
                int logType = request.getLogType();
                if(logType == Constant.NO_LOG) {                                                                       //如果用户设置了日志类型，则需要打印日志，这个日志类型还需要设定，靠下面的logger的格式设置
                    return;
                }else{
                    String[] dataComponentIds = Constant.DATA_COMPONENT_IDs;
                    List<DataComponent> dataComponents = new ArrayList<>();
                    for (String dataComponentId : dataComponentIds) {
                        DataComponent dataComponent = componentConstant.getDataComponent(dataComponentId, contextOwner);
                        if(dataComponent != null){
                            dataComponents.add(dataComponent);
                        }
                    }
                    if(dataComponents.size() > 0){
                        logService.writeLog(logType, dataComponents, processTrace);
                    }
                }
            }
        };
        thread.start();

        return new ComponentBizResult("END_S");
    }
}
