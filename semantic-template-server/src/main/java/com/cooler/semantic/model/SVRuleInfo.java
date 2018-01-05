package com.cooler.semantic.model;

import com.cooler.semantic.entity.RRuleEntity;

import java.util.List;

public class SVRuleInfo {

    //*****************************************************0.共有属性
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

    //*****************************************************3.SentenceVector数据：此RuleInfo做为一个计算单元，以下是从句子向量SentenceVector中取出来的值，用来进行相似度计算的值
    /**
     * 原始句子
     */
    private String sentence;

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

    //*****************************************************4.此句子向量（分词方式）绑定的ruleId，后面能够去找对应的rule，跟这个句子向量进行匹配
    /**
     * 绑定的规则ID
     */
    private Integer ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 这个规则在匹配后，缺失的必须参数
     */
    private List<RRuleEntity> lackedRRuleEntities;

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
}
