package com.cooler.semantic.service.external.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.SimilarityCalculateService;
import com.cooler.semantic.service.internal.AccountConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service("similarityCalculateService")
public class SimilarityCalculateServiceImpl implements SimilarityCalculateService {

    @Autowired
    private AccountConfigurationService accountConfigurationService;
    @Override
    public List<SVRuleInfo> similarityCalculate(ContextOwner contextOwner, List<SVRuleInfo> svRuleInfos, Map<String, RRuleEntity> rRuleEntityMap) {
        AccountConfiguration accountConfiguration = accountConfigurationService.selectAIdUId(contextOwner.getAccountId(), contextOwner.getUserId());
        Integer algorithmType = accountConfiguration.getAlgorithmType();                                                //由用户选择使用哪种算法
        switch (algorithmType){
            case Constant.JACCARD_VOLUME_RATE : {
                return jaccardSimilarity(svRuleInfos, rRuleEntityMap, Constant.JACCARD_VOLUME_RATE);
            }
            case Constant.JACCARD_WEIGHT_RATE : {
                return jaccardSimilarity(svRuleInfos, rRuleEntityMap, Constant.JACCARD_WEIGHT_RATE);
            }
            case Constant.JACCARD_VOLUME_WEIGHT_RATE : {
                return jaccardSimilarity(svRuleInfos, rRuleEntityMap, Constant.JACCARD_VOLUME_WEIGHT_RATE);
            }
            case Constant.COSINE : {
                return cosineSimilarity(svRuleInfos, rRuleEntityMap);
            }
            case Constant.TFIDF : {

            }
            default:{

            }
        }
        return null;
    }

    /**
     * Jaccard相似度算法
     * @param svRuleInfos
     * @param rRuleEntityMap
     * @param typeId    JACCARD_VOLUME_RATE 只考虑实体数量占比率；JACCARD_WEIGHT_RATE 只考虑实体权重占比率； JACCARD_VOLUME_WEIGHT_RATE 考虑两则的乘积
     * @return
     */
    private List<SVRuleInfo> jaccardSimilarity(List<SVRuleInfo> svRuleInfos, Map<String, RRuleEntity> rRuleEntityMap, int typeId){
        for (SVRuleInfo svRuleInfo : svRuleInfos) {                                                                     //下面是3层循环，所以svRuleInfos在前面做了优化，限定数量最多为5个
            //1.准备好两方数据：句子向量的的数据在svRuleInfo的rEntityWordInfosList里面和外面；其绑定的rule的数据，全部放在入参rRuleEntityMap里面
            String sentence = svRuleInfo.getSentence();
            List<String> words = svRuleInfo.getWords();
            List<String> natures = svRuleInfo.getNatures();
            List<Double> weights = svRuleInfo.getWeights();

            List<List<REntityWordInfo>> rEntityWordInfosList = svRuleInfo.getrEntityWordInfosList();                    //代表了句子向量一方的数据
            int entitySize = rEntityWordInfosList.size();

            Integer ruleId = svRuleInfo.getRuleId();                                                                    //指向ruleId的这条rule

            //2.给予每个实体集合的每个实体一次机会，如果能匹配上，则 句子向量和实体本身的两端 的 单项数量占比和单项权重占 比都被积累进入 交集数量占比值和交集权重占比值中
            Double similarity = 0d;
            Double intersectionVolumeRateOccupancy = 0d;
            Double intersectionWeightOccupancy = 0d;
            for(int i = 0; i < rEntityWordInfosList.size(); i ++){                                                          //遍历svRuleInfo绑定的一个分词方式指定的实体集群
                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosList.get(i);                                        //第i个word分词段归属到的实体集合

                for(int j = 0; j < rEntityWordInfos.size(); j ++){                                                              //遍历每一个分词段指定的实体集
                    REntityWordInfo rEntityWordInfo = rEntityWordInfos.get(j);
                    Integer entityType = rEntityWordInfo.getEntityType();
                    Integer entityId = rEntityWordInfo.getEntityId();
                    String key = ruleId + "_" + entityType + "_" + entityId;

                    RRuleEntity rRuleEntity = rRuleEntityMap.get(key);                                                          //按照这个key，检索到，就表示被记录，表示能匹配上
                    if(rRuleEntity != null){
                        //记录此句子向量中归属的实体成功匹配上绑定的规则中的一个实体了 //TODO: 那么规则中的rRuleEntity也可以在db中记录这一次匹配，可以统计一个rRuleEntity的匹配次数
                        System.out.println("Matched！ : 原句" + sentence + ", 分词方式：" + Arrays.toString(words.toArray()) + JSON.toJSONString(rEntityWordInfo) + " --- " + JSON.toJSONString(rRuleEntity));

                        Double volumeRateItem = 1.0d / entitySize;                                                              //这一项在句子中的数量比重
                        Double volumeRate = rRuleEntity.getVolumeRate();                                                        //这一项在规则中的数量比重
                        intersectionVolumeRateOccupancy = intersectionVolumeRateOccupancy + volumeRateItem + volumeRate;                                //积累数量比重

                        Double sv_weight = weights.get(i);                                                                      //句子向量中，第i个word分词归属到的实体的权重
                        Double rule_weight = rRuleEntity.getWeight();                                                           //rule模板中，这个实体在rule中的权重
                        intersectionWeightOccupancy = intersectionWeightOccupancy + sv_weight + rule_weight;                    //积累权重比重
                    }
                }
            }
            intersectionVolumeRateOccupancy /= 2;
            intersectionWeightOccupancy /= 2;

            switch (typeId){
                case Constant.JACCARD_VOLUME_RATE : {
                    similarity = intersectionVolumeRateOccupancy;
                    break;
                }
                case Constant.JACCARD_WEIGHT_RATE : {
                    similarity = intersectionWeightOccupancy;
                    break;
                }
                case Constant.JACCARD_VOLUME_WEIGHT_RATE : {
                    similarity = intersectionVolumeRateOccupancy * intersectionWeightOccupancy;
                    break;
                }
                default:{
                    similarity = intersectionVolumeRateOccupancy * intersectionWeightOccupancy;
                }
            }
            svRuleInfo.setSimilarity(similarity);
        }
        return svRuleInfos;
    }

    /**
     * 余弦相似度
     * @param svRuleInfos
     * @param rRuleEntityMap
     * @return
     */
    private List<SVRuleInfo> cosineSimilarity(List<SVRuleInfo> svRuleInfos, Map<String, RRuleEntity> rRuleEntityMap){
        for (SVRuleInfo svRuleInfo : svRuleInfos) {                                                                     //下面是3层循环，所以svRuleInfos在前面做了优化，限定数量最多为5个
            //1.准备好两方数据：句子向量的的数据在svRuleInfo的rEntityWordInfosList里面和外面；其绑定的rule的数据，全部放在入参rRuleEntityMap里面
            String sentence = svRuleInfo.getSentence();
            List<String> words = svRuleInfo.getWords();
            List<String> natures = svRuleInfo.getNatures();
            List<Double> weights = svRuleInfo.getWeights();

            List<List<REntityWordInfo>> rEntityWordInfosList = svRuleInfo.getrEntityWordInfosList();                    //代表了句子向量一方的数据

            Integer ruleId = svRuleInfo.getRuleId();                                                                    //指向ruleId的这条rule

            //2.给予每个实体集合的每个实体一次机会，如果能匹配上，则填充余弦公式的分子
            Double numerator = 0d;                                                                                      //分子
            Double sv_weight_square = 0d;                                                                               //句子向量中的权重平方和
            Double rule_weight_square = 0d;                                                                             //规则中的权重平方和
            Double cosineValue = 0d;                                                                                    //cosine值（由上面3个量计算得到）

            for(int i = 0; i < rEntityWordInfosList.size(); i ++){                                                          //遍历svRuleInfo绑定的一个分词方式指定的实体集群
                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosList.get(i);                                        //第i个word分词段归属到的实体集合
                for(int j = 0; j < rEntityWordInfos.size(); j ++){                                                              //遍历每一个分词段指定的实体集
                    REntityWordInfo rEntityWordInfo = rEntityWordInfos.get(j);
                    Integer entityType = rEntityWordInfo.getEntityType();
                    Integer entityId = rEntityWordInfo.getEntityId();
                    String key = ruleId + "_" + entityType + "_" + entityId;
                    RRuleEntity rRuleEntity = rRuleEntityMap.get(key);                                                          //按照这个key，检索到，就表示被记录，表示能匹配上
                    if(rRuleEntity != null){
                        //记录此句子向量中归属的实体成功匹配上绑定的规则中的一个实体了 //TODO: 那么规则中的rRuleEntity也可以在db中记录这一次匹配，可以统计一个rRuleEntity的匹配次数
                        System.out.println("Matched！ : 原句" + sentence + ", 分词方式：" + Arrays.toString(words.toArray()) + JSON.toJSONString(rEntityWordInfo) + " --- " + JSON.toJSONString(rRuleEntity));
                        Double sv_weight = weights.get(i);                                                                      //句子向量中，第i个word分词归属到的实体的权重
                        Double rule_weight = rRuleEntity.getWeight();                                                           //rule模板中，这个实体在rule中的权重
                        numerator += sv_weight * rule_weight;
                    }
                }
            }
            for (Double weight : weights) {
                sv_weight_square += weight * weight;
            }
            Collection<RRuleEntity> rRuleEntities = rRuleEntityMap.values();
            for (RRuleEntity rRuleEntity : rRuleEntities) {
                if(rRuleEntity.getRuleId() == ruleId){
                    Double weight = rRuleEntity.getWeight();
                    rule_weight_square += weight * weight;
                }
            }
            cosineValue = numerator / (Math.sqrt(sv_weight_square) * Math.sqrt(rule_weight_square));
            svRuleInfo.setSimilarity(cosineValue);
        }
        return svRuleInfos;
    }


}
