package com.cooler.semantic.model;

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
     * 每一个词语在数据库中对应的wordId
     */
    private List<Integer> wordIds;

    /**
     * 分好词的词性数组
     */
    private List<String> natures;

    /**
     * 实体类型集合（0为词语实体、1为字符串实体、2为正则实体、3为code实体）,默认全是0
     */
    private List<Integer> entityTypes;

    /**
     * 设好权重的权重数值数组
     */
    private List<Double> weights;

//    /**
//     * 综合词段信息
//     */
//    private List<WordInfo> wordInfos;
//
//    /**
//     * 一对一关系Map，（为了多维度值计算相似度）
//     */
//    private Map<Integer, REntityWord> rEntityWordMap;
//
//    /**
//     * 一对多关系集合，（后续离散排布和转置，为了计算交集）
//     */
//    private List<R_Entities_Word> rEntitiesWords;


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

    public List<Integer> getWordIds() {
        return wordIds;
    }

    public void setWordIds(List<Integer> wordIds) {
        this.wordIds = wordIds;
    }

    public List<String> getNatures() {
        return natures;
    }

    public void setNatures(List<String> natures) {
        this.natures = natures;
    }

    public List<Integer> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<Integer> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }
}
