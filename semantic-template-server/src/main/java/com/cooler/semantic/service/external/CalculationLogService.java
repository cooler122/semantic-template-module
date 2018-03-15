package com.cooler.semantic.service.external;

import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.model.ContextOwner;

import java.util.List;

public interface CalculationLogService {
    /**
     * 根据日志类型，来写日志
     * @param logType   日志类型
     * @param dataComponents    数据组件
     * @param processTrace  轨迹
     */
    void writeLog(ContextOwner contextOwner, int logType, List<DataComponent<String>> dataComponents, String processTrace, long currentTimeMillis);
}
