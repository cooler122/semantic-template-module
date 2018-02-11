package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.service.external.CalculationLogService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("calculationLogService")
public class CalculationLogServiceImpl implements CalculationLogService {
    
    @Override
    public void writeLog(int logType, List<DataComponent> dataComponents, String processTrace) {
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
                //writeDataBaseLog(dataComponents, processTrace);
                break;
            }
            default : {
                //writeTextLog(dataComponents, processTrace);
            }
        }
        
    }
}
