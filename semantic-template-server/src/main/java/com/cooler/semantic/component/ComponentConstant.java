package com.cooler.semantic.component;

import com.cooler.semantic.component.biz.FunctionComponent;
import com.cooler.semantic.component.data.DataComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class ComponentConstant {

    /**
     * 循环次数
     */
    public static int loopCount = 0;
    /**
     * 数据组件Map
     */
    private Map<String, DataComponent> dataBeanMap = new HashMap<>();
    /**
     * 功能组件Map
     */
    private static Map<String, FunctionComponent> functionComponentMap = new HashMap<>();
    /**
     * 流程关系Map
     */
    private static Map<String, String> nextComponentIdMap = new HashMap<>();

    public Map<String, DataComponent> getDataBeanMap() {
        return dataBeanMap;
    }

    public void setDataBeanMap(Map<String, DataComponent> dataBeanMap) {
        this.dataBeanMap = dataBeanMap;
    }

    public Map<String, FunctionComponent> getFunctionComponentMap() {
        return functionComponentMap;
    }

    public void setFunctionComponentMap(Map<String, FunctionComponent> functionComponentMap) {
        this.functionComponentMap = functionComponentMap;
    }

    public Map<String, String> getNextComponentIdMap() {
        return nextComponentIdMap;
    }

    public void setNextComponentIdMap(Map<String, String> nextComponentIdMap) {
        this.nextComponentIdMap = nextComponentIdMap;
    }
}
