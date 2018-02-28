package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.dao.LogDataCalculationMapper;
import com.cooler.semantic.entity.LogDataCalculation;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.service.external.CalculationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("calculationLogService")
public class CalculationLogServiceImpl implements CalculationLogService {
    private static Logger logger = LoggerFactory.getLogger(CalculationLogServiceImpl.class.getName());
    @Autowired
    private LogDataCalculationMapper logDataCalculationMapper;
    @Override
    public void writeLog(int logType, List<DataComponent<String>> dataComponents, String processTrace) {
        switch (logType){
            case Constant.CALCULATION_LOG_TEXT : {
                //writeTextLog(dataComponents, processTrace);
                break;
            }
            case Constant.CALCULATION_LOG_HTML : {
                //writeHtmlLog(dataComponents, processTrace);
                break;
            }
            case Constant.CALCULATION_LOG_DATA_BASE : {
                writeDataBaseLog(dataComponents, processTrace);
                break;
            }
            default : {
                //writeTextLog(dataComponents, processTrace);
            }
        }
    }

    private void writeDataBaseLog(List<DataComponent<String>> dataComponents, String processTrace) {
        LogDataCalculation logDataCalculation = new LogDataCalculation();
        for (DataComponent<String> dataComponent : dataComponents) {
            String dataComponentId = dataComponent.getId();
            ContextOwner contextOwner = dataComponent.getContextOwner();
            String data = dataComponent.getData();
            logDataCalculation.setDateTime(new Date());
            logDataCalculation.setAccountId(contextOwner.getCoreAccountId());
            logDataCalculation.setUserId(contextOwner.getUserId());
            logDataCalculation.setContextId(contextOwner.getContextId());
            logDataCalculation.setProcessTrace(processTrace);
            logDataCalculation.setDetailContextOwner(Arrays.toString(contextOwner.getAccountIds().toArray()) + "_" + contextOwner.getUserId() + "_" + contextOwner.getContextId());
            if(dataComponentId.equals(Constant.CALCULATION_LOG_COMPONENT_IDs[0])){                                 //CalculationLogParam_LPM
                logDataCalculation.setLpmJsonData(data);
            }else if(dataComponentId.equals(Constant.CALCULATION_LOG_COMPONENT_IDs[1])){                          //CalculationLogParam_CPM
                logDataCalculation.setCpmJsonData(data);
            }else if(dataComponentId.equals(Constant.CALCULATION_LOG_COMPONENT_IDs[2])){                          //CalculationLogParam_FPM
                logDataCalculation.setFpmJsonData(data);
            }else{}
        }
        logDataCalculationMapper.insert(logDataCalculation);
    }
}
