package com.cooler.semantic.service.external.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.dao.RRuleEntityMapper;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.ContextOwner;
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
    public List<SVRuleInfo> getRulesBySentenceVectors(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        Integer accountId = contextOwner.getAccountId();
        List<SVRuleInfo> svRuleInfosTopFive = new ArrayList<>();                                                        //（前5名）真正用来计算相似度的RuleInfo对象
        List<SVRuleInfo> svRuleInfosBeyondFirstThreshold = new ArrayList<>();                                           //超过第一层规则端阈值的RuleInfo对象（收集了多种分词方式后形成的）
        for (int i = 0; i < sentenceVectors.size(); i ++) {
            SentenceVector sentenceVector = sentenceVectors.get(i);
            Integer sentenceVectorId = sentenceVector.getId();
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
                svRuleInfo.setPreRuleVolumeRateOccupancy(svRuleInfo.getPreRuleVolumeRateOccupancy() + rRuleEntity.getVolumeRate()); //经过累积，看看对于检索出来的规则能有怎样的预期实体数量占有率
                svRuleInfo.setPreRuleWeightOccupancy(svRuleInfo.getPreRuleWeightOccupancy() + rRuleEntity.getWeight());             //经过累积，看看对于检索出来的规则能有怎样的预期实体权重占有率

                if(svRuleInfo.getPreRuleVolumeRateOccupancy() >= 0.2 || svRuleInfo.getPreRuleWeightOccupancy() >= 0.34){        //规则端阈值，只有超过这两个阈值之一，才有资格建立成一个完整的RuleInfo对象，参与相似度计算，不然计算没意义//TODO:可以在用户配置建立新参数字段（是或是并，也可调节）
                    svRuleInfo.setRuleId(ruleId);                                                                       //自身数据：ruleId、ruleName、accountId、
                    svRuleInfo.setRuleName(rRuleEntity.getRuleName());
                    svRuleInfo.setAccountId(rRuleEntity.getAccountId());

                    svRuleInfo.setSentenceVectorId(sentenceVectorId);
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
                    svRuleInfosBeyondFirstThreshold.add(SVRuleInfo);
                }
            }
        }
        Collections.sort(svRuleInfosBeyondFirstThreshold, new Comparator<SVRuleInfo>() {
            @Override
            public int compare(SVRuleInfo o1, SVRuleInfo o2) {                                                        //倒序排序，见"if(o_product > product) return 1;"
                Double product = o1.getPreRuleVolumeRateOccupancy() * o1.getPreRuleWeightOccupancy();
                Double o_product = o2.getPreRuleVolumeRateOccupancy() * o2.getPreRuleWeightOccupancy();
                if(o_product > product) return 1;
                else if(product == o_product) return 0;
                else return -1;
            }
        });                                                                                                             //排序（倒排），获取预期值最大的前5个
        for (int i = 0; i < svRuleInfosBeyondFirstThreshold.size() && i < 5; i ++) {
            svRuleInfosTopFive.add(svRuleInfosBeyondFirstThreshold.get(i));
        }

        return svRuleInfosTopFive;
    }
}
