package com.cooler.semantic.service.external.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.dao.RRuleEntityMapper;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.entity.Rule;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.RuleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ruleSearchService")
public class RuleSearchServiceImpl implements RuleSearchService {

    @Autowired
    private RRuleEntityMapper rRuleEntityMapper;

    @Override
    public List<Rule> getRulesBySentenceVectors(Integer accountId, List<SentenceVector> sentenceVectors) {

        for (int i = 0; i < sentenceVectors.size(); i ++) {
            SentenceVector sentenceVector = sentenceVectors.get(i);
            List<List<REntityWordInfo>> rEntityWordInfosList = sentenceVector.getrEntityWordInfosList();                //查出来每个分词模式相关的RRE集合

            List<REntityWordInfo> rEntityWordInfosParam = new ArrayList<>();
            for (List<REntityWordInfo> entityWordInfos : rEntityWordInfosList) {
                rEntityWordInfosParam.addAll(entityWordInfos);
            }
            List<RRuleEntity> rRuleEntities = rRuleEntityMapper.selectByREntityWordInfos(accountId, rEntityWordInfosParam);

            Map<Integer, Integer> ruleIdCountMap = new HashMap<>();
            Map<Integer, Double> ruleIdWeightMap = new HashMap<>();

            Integer maximumRuleId = null;
            Integer highestRuleId = null;
            int maximumIdCount = 0;
            double highestIdWeight = 0d;

            for (RRuleEntity rRuleEntity : rRuleEntities) {
                Integer ruleId = rRuleEntity.getRuleId();
                Integer count = ruleIdCountMap.get(ruleId);                                 //获取ruleId的实体当前数量
                Double weight = ruleIdWeightMap.get(ruleId);                                //获取ruleId的实体当前权值和

                if(count == null){
                    count = 1;
                    weight = rRuleEntity.getWeight();
                }else{
                    count ++;
                    weight += rRuleEntity.getWeight();
                }
                ruleIdCountMap.put(ruleId, count);
                ruleIdWeightMap.put(ruleId, weight);

                if(count > maximumIdCount) {
                    maximumIdCount = count;
                    maximumRuleId = ruleId;
                }
                if(weight > highestIdWeight) {
                    highestIdWeight = weight;
                    highestRuleId = ruleId;
                }
            }

            System.out.println(JSON.toJSONString(ruleIdCountMap));
            System.out.println(JSON.toJSONString(ruleIdWeightMap));
            System.out.println("第 " + (i + 1) + " 种分词方式，实体数量最广的ruleId： " + maximumRuleId + " ，数量：" + maximumIdCount + " ，权值和最高的ruleId： " + highestRuleId + " 权值： " + highestIdWeight);        }

        return null;
    }
}
