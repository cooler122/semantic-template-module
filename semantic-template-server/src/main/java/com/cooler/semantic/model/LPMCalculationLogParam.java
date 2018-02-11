package com.cooler.semantic.model;

import com.cooler.semantic.component.data.DataComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LPMCalculationLogParam {
    //------------------------------------------------------------------------------------------------------------------1.原始数据
    /**
     *  句子向量端的REWIs
     */
    private List<SentenceVector> sentenceVectors = new ArrayList<>();

    /**
     * 每个历史缺参rule端的历史记录
     */
    private Map<Integer, DataComponent<SVRuleInfo>> dataComponentMap = new HashMap<>();

    //------------------------------------------------------------------------------------------------------------------2.（集合）初级筛选以及合并过程的变动率数据（调参门槛）
    private List<CoupleAlterationRateData> coupleDatas = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------3.（集合）失忆系数相关
    private List<AmnesiacData> amnesiacDatas = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------4.（集合）相似度计算中数据
    private List<SimilarityCalculationData> similarityCalculationDatas = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------5.最优数据
    private SVRuleInfo lpmSVRuleInfo;


    //------------------------------------------------------------------------------------------------------------------gets、sets
    public List<SentenceVector> getSentenceVectors() {
        return sentenceVectors;
    }

    public void setSentenceVectors(List<SentenceVector> sentenceVectors) {
        this.sentenceVectors = sentenceVectors;
    }

    public Map<Integer, DataComponent<SVRuleInfo>> getDataComponentMap() {
        return dataComponentMap;
    }

    public void setDataComponentMap(Map<Integer, DataComponent<SVRuleInfo>> dataComponentMap) {
        this.dataComponentMap = dataComponentMap;
    }

    public List<CoupleAlterationRateData> getCoupleDatas() {
        return coupleDatas;
    }

    public void setCoupleDatas(List<CoupleAlterationRateData> coupleDatas) {
        this.coupleDatas = coupleDatas;
    }

    public List<AmnesiacData> getAmnesiacDatas() {
        return amnesiacDatas;
    }

    public void setAmnesiacDatas(List<AmnesiacData> amnesiacDatas) {
        this.amnesiacDatas = amnesiacDatas;
    }

    public List<SimilarityCalculationData> getSimilarityCalculationDatas() {
        return similarityCalculationDatas;
    }

    public void setSimilarityCalculationDatas(List<SimilarityCalculationData> similarityCalculationDatas) {
        this.similarityCalculationDatas = similarityCalculationDatas;
    }

    public SVRuleInfo getLpmSVRuleInfo() {
        return lpmSVRuleInfo;
    }

    public void setLpmSVRuleInfo(SVRuleInfo lpmSVRuleInfo) {
        this.lpmSVRuleInfo = lpmSVRuleInfo;
    }

}
