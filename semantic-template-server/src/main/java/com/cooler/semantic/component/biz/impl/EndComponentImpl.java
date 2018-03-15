package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.service.external.CalculationLogService;
import com.cooler.semantic.service.external.ProcessLogService;
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
    private ProcessLogService processLogService;
    @Autowired
    private CalculationLogService calculationLogService;

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
                List<Integer> accountIds = request.getAccountIds();
                String processTrace = componentConstant.getTraceByContextOwnerIndex(contextOwner.getOwnerIndex());
                int processLogType = request.getProcessLogType();
                int calculationLogType = request.getCalculationLogType();
                long currentTimeMillis = System.currentTimeMillis();

                //2.打印流程日志
                if(processLogType != Constant.NO_PROCESS_LOG){
                    String[] dataComponentIds = Constant.DATA_COMPONENT_IDs;
                    List<DataComponent> dataComponents = new ArrayList<>();
                    for (String dataComponentId : dataComponentIds) {
                        DataComponent dataComponent = componentConstant.getDataComponent(dataComponentId, contextOwner);
                        if(dataComponent != null){
                            dataComponents.add(dataComponent);
                        }
                    }
                    if(dataComponents.size() > 0){
                        processLogService.writeLog(contextOwner, processLogType, dataComponents, processTrace, currentTimeMillis);
                    }
                }

                //3.打印计算日志
                if(calculationLogType != Constant.NO_CALCULATION_LOG){
                    String[] calculation_log_component_ids = Constant.CALCULATION_LOG_COMPONENT_IDs;
                    List<DataComponent<String>> dataComponents = new ArrayList<>();
                    for (String componentId : calculation_log_component_ids) {
                        DataComponent<String> dataComponent = componentConstant.getDataComponent(componentId, contextOwner);
                        if(dataComponent != null){
                            dataComponents.add(dataComponent);
                        }
                    }
                    calculationLogService.writeLog(contextOwner, calculationLogType, dataComponents, processTrace, currentTimeMillis);
                }
            }
        };
        thread.start();

        return new ComponentBizResult("END_S");
    }
}
