package com.cooler.semantic.model;

import java.util.List;

public class SVRuleInfo implements Comparable<SVRuleInfo>{

    //*****************************************************0.共有属性
    /**
     * 账户ID
     */
    private Integer accountId;

    //*****************************************************1.作为粗选凭证参数，1.按照preVolumeRateOccupancy * preWeightOccupancy的值进行竞争，前n（配置设置）名参与相似度计算，其他舍弃
    /**
     * 规则中 有效实体数量 预期占有率
     */
    private Double preVolumeRateOccupancy = 0d;

    /**
     * 规则中 有效权重 预期占有率
     */
    private Double preWeightOccupancy = 0d;

    //*****************************************************2.SentenceVector数据：此RuleInfo做为一个计算单元，以下是从句子向量SentenceVector中取出来的值，用来进行相似度计算的值
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

    //*****************************************************0.本身属性，待着ruleId，后面能够去找对应的rule
    /**
     * 规则ID
     */
    private Integer ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

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

    public Double getPreVolumeRateOccupancy() {
        return preVolumeRateOccupancy;
    }

    public void setPreVolumeRateOccupancy(Double preVolumeRateOccupancy) {
        this.preVolumeRateOccupancy = preVolumeRateOccupancy;
    }

    public Double getPreWeightOccupancy() {
        return preWeightOccupancy;
    }

    public void setPreWeightOccupancy(Double preWeightOccupancy) {
        this.preWeightOccupancy = preWeightOccupancy;
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

    @Override
    public int compareTo(SVRuleInfo o) {                                                                                //倒序排序，见"if(o_product > product) return 1;"
        Double product = this.getPreVolumeRateOccupancy() * this.getPreWeightOccupancy();
        Double o_product = o.getPreVolumeRateOccupancy() * o.getPreWeightOccupancy();
        if(o_product > product) return 1;
        else if(product == o_product) return 0;
        else return -1;
    }
}
