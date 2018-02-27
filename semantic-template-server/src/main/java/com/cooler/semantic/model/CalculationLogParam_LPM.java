package com.cooler.semantic.model;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.model.console.AmnesiacData;
import com.cooler.semantic.model.console.CoupleAlterationRateData;
import com.cooler.semantic.model.console.SimilarityCalculationData_LPM;

import java.util.*;

public class CalculationLogParam_LPM {

    //------------------------------------------------------------------------------------------------------------------1.原始数据
    /**
     *  句子向量端的REWIs
     */
    private List<SentenceVector> sentenceVectors = new ArrayList<>();

    /**
     * 每个历史缺参rule端的历史记录
     */
    private Map<Integer, SVRuleInfo> historySVRuleInfoMap = new HashMap<>();

    /**
     * 取消的缺参上下文ID
     */
    private Set<Integer> canceledLPMContextIdSet = new HashSet<>();

    //------------------------------------------------------------------------------------------------------------------2.（集合）初级筛选以及合并过程的变动率数据（调参门槛）
    private List<CoupleAlterationRateData> coupleAlterationRateDatas = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------3.（集合）失忆系数相关
    private List<AmnesiacData> amnesiacDatas = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------4.（集合）相似度计算中数据
    private List<SimilarityCalculationData_LPM> similarityCalculationDataLPMS = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------5.最优数据
    private SVRuleInfo lpmSVRuleInfo;

    //------------------------------------------------------------------------------------------------------------------6.最终上面形成的json数据的Map，上面集合的其key值形式为"sentenceVectorId_contextId_日志类型标识"，用来查询
    private Map<String, String> jsonDataMap = new HashMap<>();


    //------------------------------------------------------------------------------------------------------------------gets、sets
    public List<SentenceVector> getSentenceVectors() {
        return sentenceVectors;
    }

    public void setSentenceVectors(List<SentenceVector> sentenceVectors) {
        this.sentenceVectors = sentenceVectors;
    }

    public Map<Integer, SVRuleInfo> getHistorySVRuleInfoMap() {
        return historySVRuleInfoMap;
    }

    public void setHistorySVRuleInfoMap(Map<Integer, SVRuleInfo> historySVRuleInfoMap) {
        this.historySVRuleInfoMap = historySVRuleInfoMap;
    }

    public Set<Integer> getCanceledLPMContextIdSet() {
        return canceledLPMContextIdSet;
    }

    public void setCanceledLPMContextIdSet(Set<Integer> canceledLPMContextIdSet) {
        this.canceledLPMContextIdSet = canceledLPMContextIdSet;
    }

    public List<CoupleAlterationRateData> getCoupleAlterationRateDatas() {
        return coupleAlterationRateDatas;
    }

    public void setCoupleAlterationRateDatas(List<CoupleAlterationRateData> coupleAlterationRateDatas) {
        this.coupleAlterationRateDatas = coupleAlterationRateDatas;
    }

    public List<AmnesiacData> getAmnesiacDatas() {
        return amnesiacDatas;
    }

    public void setAmnesiacDatas(List<AmnesiacData> amnesiacDatas) {
        this.amnesiacDatas = amnesiacDatas;
    }

    public List<SimilarityCalculationData_LPM> getSimilarityCalculationDataLPMS() {
        return similarityCalculationDataLPMS;
    }

    public void setSimilarityCalculationDataLPMS(List<SimilarityCalculationData_LPM> similarityCalculationDataLPMS) {
        this.similarityCalculationDataLPMS = similarityCalculationDataLPMS;
    }

    public SVRuleInfo getLpmSVRuleInfo() {
        return lpmSVRuleInfo;
    }

    public void setLpmSVRuleInfo(SVRuleInfo lpmSVRuleInfo) {
        this.lpmSVRuleInfo = lpmSVRuleInfo;
    }

    public Map<String, String> getJsonDataMap() {
        jsonDataMap.put("sentenceVectors", JSON.toJSONString(sentenceVectors));
        jsonDataMap.put("historySVRuleInfoMap", JSON.toJSONString(historySVRuleInfoMap));
        jsonDataMap.put("canceledLPMContextIdSet", JSON.toJSONString(canceledLPMContextIdSet));
        for (CoupleAlterationRateData coupleAlterationRateData : coupleAlterationRateDatas) {
            Integer sentenceVectorId = coupleAlterationRateData.getSentenceVectorId();
            Integer contextId = coupleAlterationRateData.getContextId();
            jsonDataMap.put("coupleAlterationRateData_" + sentenceVectorId + "_" + contextId, JSON.toJSONString(coupleAlterationRateData));
        }
        for (AmnesiacData amnesiacData : amnesiacDatas) {
            Integer currentContextId = amnesiacData.getCurrentContextId();
            Integer historyContextId = amnesiacData.getHistoryContextId();
            jsonDataMap.put("amnesiacData_" + currentContextId + "_" + historyContextId, JSON.toJSONString(amnesiacData));
        }
        for (SimilarityCalculationData_LPM similarityCalculationDataLPM : similarityCalculationDataLPMS) {
            Integer sentenceVectorId = similarityCalculationDataLPM.getSentenceVectorId();
            Integer contextId = similarityCalculationDataLPM.getContextId();
            jsonDataMap.put("similarityCalculationData_" + sentenceVectorId + "_" + contextId, JSON.toJSONString(similarityCalculationDataLPM));
        }
        jsonDataMap.put("lpmSVRuleInfo", JSON.toJSONString(lpmSVRuleInfo));
        return jsonDataMap;
    }

    public void setJsonDataMap(Map<String, String> jsonDataMap) {
        this.jsonDataMap = jsonDataMap;


    }
}
