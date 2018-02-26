package com.cooler.semantic.service.external;

import com.cooler.semantic.component.data.DataComponent;

import java.util.List;

public interface ProcessLogService {
    /**
     * 根据日志类型，来写日志
     * @param logType   日志类型
     * @param dataComponents    数据组件
     * @param processTrace  轨迹
     */
    void writeLog(int logType, List<DataComponent> dataComponents, String processTrace);
}
