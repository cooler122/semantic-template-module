package com.cooler.semantic.component;

import com.cooler.semantic.component.biz.FunctionComponent;
import com.cooler.semantic.component.data.DataComponent;

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
    public static Map<String, DataComponent> dataBeanMap = new HashMap<>();
    /**
     * 功能组件Map
     */
    public static Map<String, FunctionComponent> functionComponentMap = new HashMap<>();
    /**
     * 流程关系Map
     */
    public static Map<String, String> nextComponentIdMap = new HashMap<>();

}
