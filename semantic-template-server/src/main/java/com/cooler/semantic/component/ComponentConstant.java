package com.cooler.semantic.component;

import com.cooler.semantic.component.biz.SemanticComponent;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.model.ContextOwner;

import java.util.HashMap;
import java.util.Map;

public class ComponentConstant {

    /**
     * 轨迹跟踪字符串Map
     */
    private Map<String, StringBuffer> traceSBMap = new HashMap<>();
    /**
     * 数据组件Map（以后用redis代替）
     */
    private Map<String, DataComponent> dataBeanMap = new HashMap<>();
    /**
     * 功能组件Map
     */
    private static Map<String, SemanticComponent> functionComponentMap = new HashMap<>();
    /**
     * 流程关系Map
     */
    private static Map<String, String> nextComponentIdMap = new HashMap<>();

    /**
     * 针对数据Map的插入入口(对外接口)
     * @param dataComponent     数据组件
     */
    public void putDataComponent(DataComponent dataComponent){
        dataBeanMap.put(dataComponent.getId() + "_" + dataComponent.getContextOwner().getOwnerIndex(), dataComponent);  //例如：semanticParserRequest_1_233_3（第1个账户下的第233个用户下的第3轮上下文）
    }

    /**
     * 针对数据Map的查询入口(对外接口)
     * @param dataComponentId   数据组件ID
     * @param ownerIndex     数据组件拥有者
     * @return  数据组件
     */
    public DataComponent getDataComponent(String dataComponentId, String ownerIndex){
        return dataBeanMap.get(dataComponentId + "_" + ownerIndex);
    }

    /**
     * 针对数据Map的查询入口(对外接口)
     * @param dataComponentId   数据组件ID
     * @param contextOwner     数据组件拥有者对象
     * @return  数据组件
     */
    public DataComponent getDataComponent(String dataComponentId, ContextOwner contextOwner){
        return dataBeanMap.get(dataComponentId + "_" + contextOwner.getOwnerIndex());
    }

    /**
     * 通过功能组件ID获取一个功能组件(对外接口)
     * @param componentId   功能组件ID
     * @return  功能组件对象
     */
    public SemanticComponent getFunctionComponent(String componentId){
        return functionComponentMap.get(componentId);
    }


    /**
     * 通过结果码，获取功能组件ID(对外接口)
     * @param resultCode    上一次的功能组件执行结果码
     * @return  结果码对应的功能组件ID
     */
    public String getFCIDByResultCode(String resultCode){
        return nextComponentIdMap.get(resultCode);
    }

    public void setTraceByContextOwnerIndex(String contextOwnerIndex, String processCode){

        StringBuffer stringBuffer = traceSBMap.get(contextOwnerIndex);
        if(stringBuffer == null){
            stringBuffer = new StringBuffer("START");
            traceSBMap.put(contextOwnerIndex, stringBuffer);
        }
        stringBuffer.append("   --->    ").append(processCode);
    }

    public String getTraceByContextOwnerIndex(String contextOwnerIndex){
        StringBuffer stringBuffer = traceSBMap.get(contextOwnerIndex);
        return stringBuffer.toString();
    }

    public void setDataBeanMap(Map<String, DataComponent> dataBeanMap) {
        this.dataBeanMap = dataBeanMap;
    }

    public void setFunctionComponentMap(Map<String, SemanticComponent> functionComponentMap) {
        this.functionComponentMap = functionComponentMap;
    }

    public void setNextComponentIdMap(Map<String, String> nextComponentIdMap) {
        this.nextComponentIdMap = nextComponentIdMap;
    }

    public Map<String, DataComponent> getDataBeanMap() {
        return dataBeanMap;
    }

    public static Map<String, SemanticComponent> getFunctionComponentMap() {
        return functionComponentMap;
    }

    public static Map<String, String> getNextComponentIdMap() {
        return nextComponentIdMap;
    }
}
