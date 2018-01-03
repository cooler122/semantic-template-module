package com.cooler.semantic.service.external.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.dao.RRuleEntityMapper;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.RuleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ruleSearchService")
public class RuleSearchServiceImpl implements RuleSearchService {

    @Autowired
    private RRuleEntityMapper rRuleEntityMapper;

    @Override
    public List<SVRuleInfo> getRulesBySentenceVectors(Integer accountId, List<SentenceVector> sentenceVectors) {

        List<SVRuleInfo> svRuleInfosTopFive = new ArrayList<>();                                                            //（前5名）真正用来计算相似度的RuleInfo对象
        List<SVRuleInfo> SVRuleInfosBeyondFirstThreshold = new ArrayList<>();                                               //超过第一层规则端阈值的RuleInfo对象（收集了多种分词方式后形成的）
        for (int i = 0; i < sentenceVectors.size(); i ++) {
            SentenceVector sentenceVector = sentenceVectors.get(i);
            String sentence = sentenceVector.getSentence();
            List<String> words = sentenceVector.getWords();
            List<String> natures = sentenceVector.getNatures();
            List<Double> weights = sentenceVector.getWeights();

            List<List<REntityWordInfo>> rEntityWordInfosList = sentenceVector.getrEntityWordInfosList();                //查出来每个分词模式相关的RRE集合

            List<REntityWordInfo> rEntityWordInfosParam = new ArrayList<>();
            for (List<REntityWordInfo> entityWordInfos : rEntityWordInfosList) {
                rEntityWordInfosParam.addAll(entityWordInfos);
            }
            List<RRuleEntity> rRuleEntities = rRuleEntityMapper.selectByREntityWordInfos(accountId, rEntityWordInfosParam);         //TODO:这句查询业务完全可以放到ES里面去做

            Map<Integer, SVRuleInfo> ruleInfoMap = new HashMap<>();                                                     //记录每一个ruleId下的乘积
            for (RRuleEntity rRuleEntity : rRuleEntities) {
                Integer ruleId = rRuleEntity.getRuleId();
                SVRuleInfo svRuleInfo = ruleInfoMap.get(ruleId);
                if(svRuleInfo == null)  svRuleInfo = new SVRuleInfo();
                svRuleInfo.setPreVolumeRateOccupancy(svRuleInfo.getPreVolumeRateOccupancy() + rRuleEntity.getVolumeRate());
                svRuleInfo.setPreWeightOccupancy(svRuleInfo.getPreWeightOccupancy() + rRuleEntity.getWeight());

                if(svRuleInfo.getPreWeightOccupancy() >= 0.35 && svRuleInfo.getPreVolumeRateOccupancy() >= 0.2){                      //规则端阈值，只有超过这两个阈值之一，才有资格建立成一个完整的RuleInfo对象，参与相似度计算，不然计算没意义（是或是并，也可调节）
                    svRuleInfo.setRuleId(ruleId);                                                                       //自身数据：ruleId、ruleName、accountId、
                    svRuleInfo.setRuleName(rRuleEntity.getRuleName());
                    svRuleInfo.setAccountId(rRuleEntity.getAccountId());

                    svRuleInfo.setSentence(sentence);                                                                   //装载sentenceVector数据：sentence、words、natures、weights、rEntityWordInfosList，作为计算相似度的句子向量端数值
                    svRuleInfo.setWords(words);
                    svRuleInfo.setNatures(natures);
                    svRuleInfo.setWeights(weights);
                    svRuleInfo.setrEntityWordInfosList(rEntityWordInfosList);
                }
                ruleInfoMap.put(ruleId, svRuleInfo);
            }
            Collection<SVRuleInfo> SVRuleInfos = ruleInfoMap.values();

            for (SVRuleInfo SVRuleInfo : SVRuleInfos) {
                if(SVRuleInfo.getRuleId() != null){                                                                    //筛掉没有超过规则端阈值的ruleInfo，也就是没有赋予ruleId的ruleInfo对象
                    SVRuleInfosBeyondFirstThreshold.add(SVRuleInfo);
                }
            }
        }
        Collections.sort(SVRuleInfosBeyondFirstThreshold);                                                              //排序（倒排），为下面提取出前5名做准备
        for (int i = 0; i < SVRuleInfosBeyondFirstThreshold.size() && i < 5; i ++) {
            svRuleInfosTopFive.add(SVRuleInfosBeyondFirstThreshold.get(i));
        }

        return svRuleInfosTopFive;
    }
}
