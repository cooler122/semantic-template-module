package com.cooler.semantic.model;

import com.cooler.semantic.entity.REntityWord;
import com.cooler.semantic.entity.RRuleEntity;

import java.io.Serializable;
import java.util.List;

public class SentenceVector implements Serializable, Cloneable {
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
     * （REntityWord关键数据）对象列表
     */
    private List<List<REntityWordInfo>> rEntityWordInfosList;

    /**
     * (RRuleEntity）对象列表，根据各个实体取出来的实体列表（粗取结果集）
     */
    private List<List<RRuleEntity>> rRuleEntitiesList;

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

    public List<Double> getWeights() {
        return weights;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }

    public List<List<REntityWordInfo>> getrEntityWordInfosList() {
        return rEntityWordInfosList;
    }

    public void setrEntityWordInfosList(List<List<REntityWordInfo>> rEntityWordInfosList) {
        this.rEntityWordInfosList = rEntityWordInfosList;
    }

    public List<List<RRuleEntity>> getrRuleEntitiesList() {
        return rRuleEntitiesList;
    }

    public void setrRuleEntitiesList(List<List<RRuleEntity>> rRuleEntitiesList) {
        this.rRuleEntitiesList = rRuleEntitiesList;
    }
}
