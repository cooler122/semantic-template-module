package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.dao.WordNatureRateMapper;
import com.cooler.semantic.entity.WordNatureRate;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.model.SentenceVectorParam;
import com.cooler.semantic.service.external.WeightCalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("weightCalculateService")
public class WeightCalculateServiceImpl implements WeightCalculateService {

    private Logger logger = LoggerFactory.getLogger(WeightCalculateServiceImpl.class);

    @Autowired
    private WordNatureRateMapper wordNatureRateMapper;

    private static Map<String, Double> wordClassRateMap = new HashMap<>();
    static{                                //如下为系统默认的词性乘数，会在此类加载的时候直接初始化一次。
        wordClassRateMap.put("n", 3.0);     //名词
        wordClassRateMap.put("n_other", 3.0);   //自定义分词（名词）
        wordClassRateMap.put("nba", 3.0);   //动物名
        wordClassRateMap.put("nm", 3.0);    //物品名
        wordClassRateMap.put("nr", 3.0);    //人名
        wordClassRateMap.put("ns", 3.0);    //地名
        wordClassRateMap.put("ntc", 3.0);   //公司名
        wordClassRateMap.put("rvt", 3.0);   //时间疑问代词
        wordClassRateMap.put("rzt", 3.0);   //时间指示代词
        wordClassRateMap.put("t", 3.0);   //时间词
        wordClassRateMap.put("tg", 1.5);   //时间词性语素

        wordClassRateMap.put("v", 3.0);     //动词
        wordClassRateMap.put("vd", 2.0);    //副动词
        wordClassRateMap.put("vg", 2.0);    //动词性语素
        wordClassRateMap.put("vi", 3.0);    //不及物动词

        wordClassRateMap.put("r", 3.0);     //代词
        wordClassRateMap.put("rg", 3.0);    //代词性语素
        wordClassRateMap.put("ry", 1.5);    //疑问代词
        wordClassRateMap.put("rz", 1.2);    //指示代词
        wordClassRateMap.put("rr", 1.0);    //人称代词
    }

    @Override
    public List<SentenceVector> calculateWeight(Integer accountId, List<SentenceVectorParam> sentenceVectorParams) {
        //1.取出此账号下的自定义词性乘数
        List<WordNatureRate> wordNatureRates = wordNatureRateMapper.selectByAccountId(accountId);
        Map<String, Double> wordClassRateMap4User = new HashMap<>();
        wordClassRateMap4User.putAll(wordClassRateMap);                                                                 //先添加系统词性乘数Map
        if(wordNatureRates != null && wordNatureRates.size() > 0){
            for (WordNatureRate wordClassRate : wordNatureRates) {
                wordClassRateMap4User.put(wordClassRate.getWordNature(), wordClassRate.getRate());                      //再添加每一项用户词性乘数
            }
        }

        //2.对每一个词段先进行平均分配权重，再乘上词性乘数
        List<SentenceVector> sentenceVectors = new ArrayList<>();
        int svId = 0;
        for (SentenceVectorParam sentenceVectorParam : sentenceVectorParams) {
            String sentence = sentenceVectorParam.getSentence();
            List<String> words = sentenceVectorParam.getWords();
            List<String> natures = sentenceVectorParam.getNatures();
            List<Double> weights = sentenceVectorParam.getWeights();

            int size = words.size();
            double totalRatedWeight = 0.0d;
            for(int i = 0; i < size; i ++){
                double initWeight = 1d / (double) size;
                Double rate = wordClassRateMap4User.get(natures.get(i));
                if(rate != null && rate.doubleValue() != 1.0){
                    weights.set(i, initWeight * rate);
                    totalRatedWeight += (initWeight * rate);
                }else{
                    weights.set(i, initWeight);
                    totalRatedWeight += initWeight;
                }
            }

            //3.归一化
            for (int i = 0;i < size; i ++) {
                double weight = weights.get(i) / totalRatedWeight;
                BigDecimal bigDecimal = new BigDecimal(weight);
                weight = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();             //四舍五入
                weights.set(i, weight);
            }

            //4.赋值
            SentenceVector sentenceVector = new SentenceVector();
            sentenceVector.setId(svId ++);
            sentenceVector.setSentence(sentence);
            sentenceVector.setWords(words);
            sentenceVector.setNatures(natures);
            sentenceVector.setWeights(weights);
            sentenceVectors.add(sentenceVector);
        }
        return sentenceVectors;
    }
}
