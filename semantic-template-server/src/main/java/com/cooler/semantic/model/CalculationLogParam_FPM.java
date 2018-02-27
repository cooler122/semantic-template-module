package com.cooler.semantic.model;

import com.cooler.semantic.entity.RRuleEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculationLogParam_FPM {
    /**
     * 匹配到的rule和实体数据集 ---> entityData1、entityData2、entityData3、entityData4...
     */
    private List<Map<String, List<String>>> hitRuleEntityMaps = new ArrayList<>();

    /**
     * 预选出的5个SVRuleInfo集合
     */
    private List<SVRuleInfo> svRuleInfos = new ArrayList<>();

    /**
     * ruleId下面的RRE数据
     */
    private Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap = new HashMap<>();

    //------------------------------------------------------------------------------------------------------------------gets、sets

    public List<Map<String, List<String>>> getHitRuleEntityMaps() {
        return hitRuleEntityMaps;
    }

    public void setHitRuleEntityMaps(List<Map<String, List<String>>> hitRuleEntityMaps) {
        this.hitRuleEntityMaps = hitRuleEntityMaps;
    }

    public List<SVRuleInfo> getSvRuleInfos() {
        return svRuleInfos;
    }

    public void setSvRuleInfos(List<SVRuleInfo> svRuleInfos) {
        this.svRuleInfos = svRuleInfos;
    }

    public Map<Integer, Map<String, RRuleEntity>> getRuleId_RRuleEntityDataMap() {
        return ruleId_RRuleEntityDataMap;
    }

    public void setRuleId_RRuleEntityDataMap(Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap) {
        this.ruleId_RRuleEntityDataMap = ruleId_RRuleEntityDataMap;
    }
}
