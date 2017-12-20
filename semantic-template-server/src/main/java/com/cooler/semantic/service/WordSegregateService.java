package com.cooler.semantic.service;

import com.cooler.semantic.model.SentenceVector;

import java.util.List;

public interface WordSegregateService {
    /**
     * 多从自定义分词（按照1.账号ID 2.场景ID 3.意图ID 4.规则ID 来检索自定义分词是否存在于句子中）
     * @param sentence  原始句子
     * @param accountId 账号ID（必须，如果在账号ID下有分词存在于句子中，则用）
     * @param domainIds 场景-意图-规则的IDs（细粒度，可选）
     * @param selectorIds   选择的分词器IDs（用哪些分词器来分此句子）      ！！！！注意后面进行调参的时候可能会调节selectIds来提升准确率，或者调换分词器。
     * @param isDropPunctuation   是否需要去掉符号
     * @return  多个 分好词的词段集合
     */
    List<SentenceVector> wordSegregate(String sentence, Integer accountId, List<Integer> domainIds, List<Integer> selectorIds, boolean isDropPunctuation);




}
