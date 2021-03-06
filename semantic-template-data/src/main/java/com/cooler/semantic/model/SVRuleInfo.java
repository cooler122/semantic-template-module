package com.cooler.semantic.model;

import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.RRuleEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SVRuleInfo implements Serializable{

    //*****************************************************0.共有属性
//    /**
//     * 这个绑定的上下文版本号
//     */
//    private Integer contextId = null;
//
//    /**
//     * 此SVRuleInfo的来源对话编号（一般在缺参匹配过程中记录本值，通过本值强制进行过一轮缺参匹配并缺参匹配上的缺参上下文在后续对话中不再缺参匹配）
//     */
//    private Integer fromContextId = null;

    /**
     * 账户ID
     */
    private Integer accountId;

    //*****************************************************1.作为第一次和第二次的初步规则筛选凭证参数：第一次筛选，preRuleWeightOccupancy和preRuleWeightOccupancy，必须达到一定阈值才能通过。 第二次筛选，按照preRuleVolumeRateOccupancy * preRuleWeightOccupancy的值进行竞争，前n（配置设置）名参与相似度计算，其他舍弃
    /**
     * 规则中 有效实体数量 预期占有率
     */
    private Double preRuleVolumeRateOccupancy = 0d;

    /**
     * 规则中 有效权重 预期占有率
     */
    private Double preRuleWeightOccupancy = 0d;

    //*****************************************************2.作为计算相似度的结果参数：按照intersectionVolumeRateOccupancy 和 intersectionWeightOccupancy的值来选出最佳匹配的规则。
    /**
     * 句子向量和规则的交集 有效实体数量 实际占有率
     */
    private Double similarity = 0d;

    /**
     * 运行过程中获取的阈值（可能是用户设置的全局阈值，也可能是用户设置的规则性局部阈值），总之similarity > runningAccuracyThreshold，才能算匹配上
     */
    private Double runningAccuracyThreshold = 0d;

    /**
     * 此值的含义：确定它是最优结果值吗？
     * 为何有此字段？因为换参匹配在全参匹配之前，换参匹配有时候得到一个SVRuleInfo结构体但不一定是最佳的，这个时候如果再进行全参匹配，可能能得到一个分值
     * 更高的SVRuleInfo，为了保险起见，还是要走全参匹配生成一个SVRuleInfo来跟换参匹配的进行对比，哪个分高用哪个作为optimalSvRuleInfo。
     * 但也不能每次都走全参匹配，这样会降低性能，只要换参匹配能达到一定情况的时候，就无须走全参匹配了（否决全参匹配）。
     *
     * 基于上面的两类不确定的情况，就设置了此字段，此字段为false，代表不确定它是否为最终optimalSvRuleInfo，还要走全参匹配；如果为true，代表已经确定换参匹配得到的SVRuleInfo为
     * optimalSvRuleInfo了，无须走全参匹配了。
     *
     * 那么如何确定此字段的值呢？此值应该在换参匹配的逻辑体中确定，如果：
     * 1.）换参匹配中得到的 历史规则实体数 <= 当前实体数
     * 2.）如果当前句子向量的名词性实体 > 2
     * 3.）如果可换参数 在句子向量中权重利用率 < 60%
     * 中任意一个被满足，那么就设置为false，说明当前换参匹配得到的SVRuleInfo不保险， 就要让它走全参匹配流程，看看能否得到一个更高值的SVRuleInfo对象作为optimalSvRuleInfo。
     */
    private boolean ensureFinal = false;

    //*****************************************************3.SentenceVector数据：此RuleInfo做为一个计算单元，以下是从句子向量SentenceVector中取出来的值，用来进行相似度计算的值
    /**
     * 句子向量的ID
     */
    private Integer sentenceVectorId;

    /**
     * 原始句子
     */
    private String sentence;

    /**
     * 经过修改了的句子（可能会和原句一样）
     */
    private String sentenceModified;

    /**
     * 指向性规则的初始句子
     */
    private String referRuleInitSentence;

    /**
     * 分好词的字符串数组
     */
    private List<String> words;

    /**
     * 分好词的词性数组
     */
    private List<String> natures;

    /**
     * 设好权重的权重数值数组
     */
    private List<Double> weights;

    /**
     * 实体-词语关联Map（来自句子向量SentenceVector，用来进行相似度计算的值）
     */
    private List<List<REntityWordInfo>> rEntityWordInfosList = null;

    //*****************************************************4.绑定的rule相关数据，跟这个句子向量进行匹配

    /**
     * 引导意图ID（如果是长对话，那么就必须有一个引导意图）
     */
    private Integer guideIntentId;

    /**
     * 绑定的规则ID
     */
    private Integer ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 是否是长对话（事务型对话）下的规则（此对话只有在D7的时候进行设置，只要首要规则被使用，无论完全匹配与否，都会走D7）
     */
    private boolean isLongConversationRule = false;


    /**
     * 多余的词语集合
     */
    private List<WordInfo> redundantWordInfos;

    /**
     * 此分词模式，选择并，匹配上的REW集合
     */
    private List<REntityWordInfo> matchedREntityWordInfos;

    /**
     * 匹配上的RRE集合
     */
    private List<RRuleEntity> matchedRRuleEntities;

    /**
     * 这个规则在匹配后，缺失的必须参数
     */
    private List<RRuleEntity> lackedRRuleEntities;

    /**
     * 在长对话中，积累的匹配上的RRE集合，形式：Map<ruleId, {REWI1, REWI2, REWI3...}>
     */
//    private List<MatchedEntityParam> accumulatedMatchedEntityParams;
//    private Map<Integer, List<REntityWordInfo>> accumulatedRuleREWIsMap;


    private Map<String, REntityWordInfo> fixedAccumulatedMatchedREWIMap;
    /**
     * 在长对话中，积累的匹配上的RRE集合(相当于上面集合的变形）形式：Map<entityTypeId, {REWI1, REWI2, REWI3, ...}>
     */
//    private Map<String, List<REntityWordInfo>> accumulatedMatchedREWIsMap;

    /**
     * 获得的有用值积累，在解析过程中获取（用到应用中的值）
     */
    private Map<String, String> businessNecessaryParamMap = new HashMap<>();

    /**
     * 此数据体的匹配类型（在过程中赋值）（3类匹配类型：LPM、CPM、FPM）
     */
    private Integer matchType = Constant.NO_MATCH;

    /**
     * 采用了的算法类型（默认贾卡德第三类算法）
     */
    private Integer algorithmType = Constant.JACCARD_VOLUME_WEIGHT_RATE;



    public Integer getSentenceVectorId() {
        return sentenceVectorId;
    }

    public void setSentenceVectorId(Integer sentenceVectorId) {
        this.sentenceVectorId = sentenceVectorId;
    }

    public Integer getGuideIntentId() {
        return guideIntentId;
    }

    public void setGuideIntentId(Integer guideIntentId) {
        this.guideIntentId = guideIntentId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<String> getNatures() {
        return natures;
    }

    public void setNatures(List<String> natures) {
        this.natures = natures;
    }

    public List<List<REntityWordInfo>> getrEntityWordInfosList() {
        return rEntityWordInfosList;
    }

    public void setrEntityWordInfosList(List<List<REntityWordInfo>> rEntityWordInfosList) {
        this.rEntityWordInfosList = rEntityWordInfosList;
    }

    public List<WordInfo> getRedundantWordInfos() {
        return redundantWordInfos;
    }

    public void setRedundantWordInfos(List<WordInfo> redundantWordInfos) {
        this.redundantWordInfos = redundantWordInfos;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }

    public Double getPreRuleVolumeRateOccupancy() {
        return preRuleVolumeRateOccupancy;
    }

    public void setPreRuleVolumeRateOccupancy(Double preRuleVolumeRateOccupancy) {
        this.preRuleVolumeRateOccupancy = preRuleVolumeRateOccupancy;
    }

    public Double getPreRuleWeightOccupancy() {
        return preRuleWeightOccupancy;
    }

    public void setPreRuleWeightOccupancy(Double preRuleWeightOccupancy) {
        this.preRuleWeightOccupancy = preRuleWeightOccupancy;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public List<RRuleEntity> getLackedRRuleEntities() {
        return lackedRRuleEntities;
    }

    public void setLackedRRuleEntities(List<RRuleEntity> lackedRRuleEntities) {
        this.lackedRRuleEntities = lackedRRuleEntities;
    }

    public List<REntityWordInfo> getMatchedREntityWordInfos() {
        return matchedREntityWordInfos;
    }

    public void setMatchedREntityWordInfos(List<REntityWordInfo> matchedREntityWordInfos) {
        this.matchedREntityWordInfos = matchedREntityWordInfos;
    }

    public List<RRuleEntity> getMatchedRRuleEntities() {
        return matchedRRuleEntities;
    }

    public void setMatchedRRuleEntities(List<RRuleEntity> matchedRRuleEntities) {
        this.matchedRRuleEntities = matchedRRuleEntities;
    }

    public Double getRunningAccuracyThreshold() {
        return runningAccuracyThreshold;
    }

    public void setRunningAccuracyThreshold(Double runningAccuracyThreshold) {
        this.runningAccuracyThreshold = runningAccuracyThreshold;
    }

    public String getSentenceModified() {
        return sentenceModified;
    }

    public void setSentenceModified(String sentenceModified) {
        this.sentenceModified = sentenceModified;
    }

    public boolean isEnsureFinal() {
        return ensureFinal;
    }

    public void setEnsureFinal(boolean ensureFinal) {
        this.ensureFinal = ensureFinal;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public Integer getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(Integer algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getReferRuleInitSentence() {
        return referRuleInitSentence;
    }

    public void setReferRuleInitSentence(String referRuleInitSentence) {
        this.referRuleInitSentence = referRuleInitSentence;
    }

    public Map<String, String> getBusinessNecessaryParamMap() {
        return businessNecessaryParamMap;
    }

    public void setBusinessNecessaryParamMap(Map<String, String> businessNecessaryParamMap) {
        this.businessNecessaryParamMap = businessNecessaryParamMap;
    }

    public boolean getIsLongConversationRule() {
        return isLongConversationRule;
    }

    public void setIsLongConversationRule(boolean islongConversationRule) {
        isLongConversationRule = islongConversationRule;
    }

//    public Map<Integer, List<REntityWordInfo>> getAccumulatedRuleREWIsMap() {
//        return accumulatedRuleREWIsMap;
//    }
//
//    public void setAccumulatedRuleREWIsMap(Map<Integer, List<REntityWordInfo>> accumulatedRuleREWIsMap) {
//        this.accumulatedRuleREWIsMap = accumulatedRuleREWIsMap;
//    }

//    public Map<String, List<REntityWordInfo>> getAccumulatedMatchedREWIsMap() {
//        return accumulatedMatchedREWIsMap;
//    }
//
//    public void setAccumulatedMatchedREWIsMap(Map<String, List<REntityWordInfo>> accumulatedMatchedREWIsMap) {
//        this.accumulatedMatchedREWIsMap = accumulatedMatchedREWIsMap;
//    }

    public Map<String, REntityWordInfo> getFixedAccumulatedMatchedREWIMap() {
        return fixedAccumulatedMatchedREWIMap;
    }

    public void setFixedAccumulatedMatchedREWIMap(Map<String, REntityWordInfo> fixedAccumulatedMatchedREWIMap) {
        this.fixedAccumulatedMatchedREWIMap = fixedAccumulatedMatchedREWIMap;
    }

    //    public List<MatchedEntityParam> getAccumulatedMatchedEntityParams() {
//        return accumulatedMatchedEntityParams;
//    }
//
//    public void setAccumulatedMatchedEntityParams(List<MatchedEntityParam> accumulatedMatchedEntityParams) {
//        this.accumulatedMatchedEntityParams = accumulatedMatchedEntityParams;
//    }

//    public Integer getContextId() {
//        return contextId;
//    }
//
//    public void setContextId(Integer contextId) {
//        this.contextId = contextId;
//    }
//
//    public Integer getFromContextId() {
//        return fromContextId;
//    }
//
//    public void setFromContextId(Integer fromContextId) {
//        this.fromContextId = fromContextId;
//    }
}
