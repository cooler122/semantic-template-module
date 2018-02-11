package com.cooler.semantic.model;

import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.entity.Entity;

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

    //------------------------------------------------------------------------------------------------------------------inner classes
    class CoupleAlterationRateData{
        /**
         * 本轮分词ID
         */
        private Integer sentenceVectorId = null;

        /**
         * 历史上下文ID
         */
        private Integer contextId = null;

        /**
         * 合并的实体参数集
         */
        private List<Entity> combinedEntities = null;

        /**
         * SV端权重占比
         */
        private double svWeightOccupyRate = 0.0d;

        /**
         * Rule端权重占比
         */
        private double ruleWeightOccupyRate = 0.0d;

        /**
         * 合并的实体数量阈值
         */
        private int combinedEntityCountAccuracyThreshold = 0;

        /**
         * 占SV端的权重阈值
         */
        private double weightRateAccuracyThreshold4SV = 0.0d;

        /**
         * 占Rule端的权重阈值
         */
        private double weightRateAccuracyThreshold4Rule = 0.0d;

        /**
         * 是否跨越门槛
         */
        private boolean isCrossed = false;

        public Integer getSentenceVectorId() {
            return sentenceVectorId;
        }

        public void setSentenceVectorId(Integer sentenceVectorId) {
            this.sentenceVectorId = sentenceVectorId;
        }

        public Integer getContextId() {
            return contextId;
        }

        public void setContextId(Integer contextId) {
            this.contextId = contextId;
        }

        public List<Entity> getCombinedEntities() {
            return combinedEntities;
        }

        public void setCombinedEntities(List<Entity> combinedEntities) {
            this.combinedEntities = combinedEntities;
        }

        public double getSvWeightOccupyRate() {
            return svWeightOccupyRate;
        }

        public void setSvWeightOccupyRate(double svWeightOccupyRate) {
            this.svWeightOccupyRate = svWeightOccupyRate;
        }

        public double getRuleWeightOccupyRate() {
            return ruleWeightOccupyRate;
        }

        public void setRuleWeightOccupyRate(double ruleWeightOccupyRate) {
            this.ruleWeightOccupyRate = ruleWeightOccupyRate;
        }

        public int getCombinedEntityCountAccuracyThreshold() {
            return combinedEntityCountAccuracyThreshold;
        }

        public void setCombinedEntityCountAccuracyThreshold(int combinedEntityCountAccuracyThreshold) {
            this.combinedEntityCountAccuracyThreshold = combinedEntityCountAccuracyThreshold;
        }

        public double getWeightRateAccuracyThreshold4SV() {
            return weightRateAccuracyThreshold4SV;
        }

        public void setWeightRateAccuracyThreshold4SV(double weightRateAccuracyThreshold4SV) {
            this.weightRateAccuracyThreshold4SV = weightRateAccuracyThreshold4SV;
        }

        public double getWeightRateAccuracyThreshold4Rule() {
            return weightRateAccuracyThreshold4Rule;
        }

        public void setWeightRateAccuracyThreshold4Rule(double weightRateAccuracyThreshold4Rule) {
            this.weightRateAccuracyThreshold4Rule = weightRateAccuracyThreshold4Rule;
        }

        public boolean isCrossed() {
            return isCrossed;
        }

        public void setCrossed(boolean crossed) {
            isCrossed = crossed;
        }
    }

    class AmnesiacData{
        /**
         * 当前上下文ID
         */
        private Integer currentContextId;

        /**
         * 历史上下文ID
         */
        private Integer historyContextId;

        /**
         * 上下文距离
         */
        private int contextDistance;

        /**
         * 失忆算法类型
         */
        private Integer amnesiacAlgorithmType;

        public Integer getCurrentContextId() {
            return currentContextId;
        }

        public void setCurrentContextId(Integer currentContextId) {
            this.currentContextId = currentContextId;
        }

        public Integer getHistoryContextId() {
            return historyContextId;
        }

        public void setHistoryContextId(Integer historyContextId) {
            this.historyContextId = historyContextId;
        }

        public int getContextDistance() {
            return contextDistance;
        }

        public void setContextDistance(int contextDistance) {
            this.contextDistance = contextDistance;
        }

        public Integer getAmnesiacAlgorithmType() {
            return amnesiacAlgorithmType;
        }

        public void setAmnesiacAlgorithmType(Integer amnesiacAlgorithmType) {
            this.amnesiacAlgorithmType = amnesiacAlgorithmType;
        }
    }

    class SimilarityCalculationData{
        /**
         * 句子实体向量
         */
        private List<Entity> svInputEntityParams;
        /**
         * 规则实体向量
         */
        private List<Entity> ruleInputEntityParams;
        /**
         * 算法类型
         */
        private Integer algorithmType;
        /**
         * 算法公式
         */
        private String algorithmFormula;
        /**
         * 交集实体的数量占比和数量占比（此量根据算法而不同，这是贾卡德算法产生的值，这里放两个值，用字符串合起来，形式如：( entityId - entityName ) : svVolumnRate - ruleVolumnRate）
         */
        private List<String> svIntersectionEntityVolumnRate;
        /**
         * 交集实体的数量占比和权重占比（此量根据算法而不同，这是贾卡德算法产生的值，这里放两个值，用字符串合起来，形式如：( entityId - entityName ) : svWeightRate - ruleWeightRate）
         */
        private List<String> ruleIntersectionEntityWeightRate;
        /**
         * 相似度分值以及其细节值(形如：similarityValue -> intersectionVolumeRateOccupancy * intersectionWeightOccupancy
         */
        private String aboutSimilarityValue;

        public List<Entity> getSvInputEntityParams() {
            return svInputEntityParams;
        }

        public void setSvInputEntityParams(List<Entity> svInputEntityParams) {
            this.svInputEntityParams = svInputEntityParams;
        }

        public List<Entity> getRuleInputEntityParams() {
            return ruleInputEntityParams;
        }

        public void setRuleInputEntityParams(List<Entity> ruleInputEntityParams) {
            this.ruleInputEntityParams = ruleInputEntityParams;
        }

        public Integer getAlgorithmType() {
            return algorithmType;
        }

        public void setAlgorithmType(Integer algorithmType) {
            this.algorithmType = algorithmType;
        }

        public String getAlgorithmFormula() {
            return algorithmFormula;
        }

        public void setAlgorithmFormula(String algorithmFormula) {
            this.algorithmFormula = algorithmFormula;
        }

        public List<String> getSvIntersectionEntityVolumnRate() {
            return svIntersectionEntityVolumnRate;
        }

        public void setSvIntersectionEntityVolumnRate(List<String> svIntersectionEntityVolumnRate) {
            this.svIntersectionEntityVolumnRate = svIntersectionEntityVolumnRate;
        }

        public List<String> getRuleIntersectionEntityWeightRate() {
            return ruleIntersectionEntityWeightRate;
        }

        public void setRuleIntersectionEntityWeightRate(List<String> ruleIntersectionEntityWeightRate) {
            this.ruleIntersectionEntityWeightRate = ruleIntersectionEntityWeightRate;
        }

        public String getAboutSimilarityValue() {
            return aboutSimilarityValue;
        }

        public void setAboutSimilarityValue(String aboutSimilarityValue) {
            this.aboutSimilarityValue = aboutSimilarityValue;
        }
    }
}
