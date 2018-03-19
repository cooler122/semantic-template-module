package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.REntityWord;
import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.CalculationLogParam_FPM;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.model.WordInfo;
import com.cooler.semantic.model.console.SimilarityCalculationData_FPM;
import com.cooler.semantic.model.console.SimilarityCalculationData_LPM;
import com.cooler.semantic.service.external.SimilarityCalculateService;
import com.cooler.semantic.util.AlgorithmUtil;
import org.springframework.stereotype.Service;
import java.util.*;

@Service("similarityCalculateService")
public class SimilarityCalculateServiceImpl implements SimilarityCalculateService {

    @Override
    public List<SVRuleInfo> similarityCalculate_FPM(Integer algorithmType, List<SVRuleInfo> svRuleInfos, Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap, SimilarityCalculationData_FPM similarityCalculationData_fpm) {
        switch (algorithmType){
            case Constant.JACCARD_VOLUME_RATE : {                                                                   //jaccard相似度（只关注实体数量占有率）
                return jaccardSimilarity_FPM(svRuleInfos, ruleId_RRuleEntityDataMap, Constant.JACCARD_VOLUME_RATE, similarityCalculationData_fpm);
            }
            case Constant.JACCARD_WEIGHT_RATE : {                                                                   //jaccard相似度（只关注实体权重占有率）
                return jaccardSimilarity_FPM(svRuleInfos, ruleId_RRuleEntityDataMap, Constant.JACCARD_WEIGHT_RATE, similarityCalculationData_fpm);
            }
            case Constant.JACCARD_VOLUME_WEIGHT_RATE : {                                                           //jaccard相似度（实体数量和权重占有率之乘积）
                return jaccardSimilarity_FPM(svRuleInfos, ruleId_RRuleEntityDataMap, Constant.JACCARD_VOLUME_WEIGHT_RATE, similarityCalculationData_fpm);            //TODO:jaccard算法的两个因子还可以用更复杂的组合方式进行调节
            }
            case Constant.COSINE : {                                                                                   //余弦相似度
                return cosineSimilarity_FPM(svRuleInfos, ruleId_RRuleEntityDataMap);
            }
            case Constant.PEARSON : {                                                                                 //皮尔逊相似度
                return pearsonSimilarity_FPM(svRuleInfos, ruleId_RRuleEntityDataMap);
            }
            default:{
                return jaccardSimilarity_FPM(svRuleInfos, ruleId_RRuleEntityDataMap, Constant.JACCARD_VOLUME_WEIGHT_RATE, similarityCalculationData_fpm);
            }
        }
    }

    @Override
    public SVRuleInfo similarityCalculate_LPM(Integer algorithmType, SVRuleInfo historySvRuleInfo, Map<String, RRuleEntity> rRuleEntityMap, SimilarityCalculationData_LPM similarityCalculationData_lpm) {
        switch (algorithmType){
            case Constant.JACCARD_VOLUME_RATE : {                                                                   //jaccard相似度（只关注实体数量占有率）
                return jaccardSimilarity_LMP(historySvRuleInfo, rRuleEntityMap, Constant.JACCARD_VOLUME_RATE, similarityCalculationData_lpm);
            }
            case Constant.JACCARD_WEIGHT_RATE : {                                                                   //jaccard相似度（只关注实体权重占有率）
                return jaccardSimilarity_LMP(historySvRuleInfo, rRuleEntityMap, Constant.JACCARD_WEIGHT_RATE, similarityCalculationData_lpm);
            }
            case Constant.JACCARD_VOLUME_WEIGHT_RATE : {                                                           //jaccard相似度（实体数量和权重占有率之乘积）
                return jaccardSimilarity_LMP(historySvRuleInfo, rRuleEntityMap, Constant.JACCARD_VOLUME_WEIGHT_RATE, similarityCalculationData_lpm);            //TODO:jaccard算法的两个因子还可以用更复杂的组合方式进行调节
            }
            case Constant.COSINE : {                                                                                   //余弦相似度
                return cosineSimilarity_LMP(historySvRuleInfo, rRuleEntityMap);
            }
            case Constant.PEARSON : {                                                                                 //皮尔逊相似度
                return pearsonSimilarity_LMP(historySvRuleInfo, rRuleEntityMap);
            }
            default:{
                return jaccardSimilarity_LMP(historySvRuleInfo, rRuleEntityMap, Constant.JACCARD_VOLUME_WEIGHT_RATE, similarityCalculationData_lpm);
            }
        }
    }

//****************************************************************************************************************************全参匹配算法
    /**
     * Jaccard相似度算法
     * @param svRuleInfos
     * @param ruleId_RRuleEntityDataMap
     * @param typeId    JACCARD_VOLUME_RATE 只考虑实体数量占比率；JACCARD_WEIGHT_RATE 只考虑实体权重占比率； JACCARD_VOLUME_WEIGHT_RATE 考虑两则的乘积
     * @return
     */
    private List<SVRuleInfo> jaccardSimilarity_FPM(List<SVRuleInfo> svRuleInfos, Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap, int typeId, SimilarityCalculationData_FPM similarityCalculationData_fpm){
        boolean enableCalculateLog = false;
        Map<String, List<REntityWordInfo>> ids_rewDatasMap = null;
        Map<String, String> ids_scoreMap = null;
        if(similarityCalculationData_fpm != null){                                                                     //如果此对象不为空，则代表当前需要打印计算日志
            enableCalculateLog = true;
            ids_rewDatasMap = new HashMap<>();
            ids_scoreMap = new HashMap<>();
        }

        for (SVRuleInfo svRuleInfo : svRuleInfos) {                                                                     //下面是3层循环，所以svRuleInfos在前面做了优化，限定数量最多为5个
            //1.准备好两方数据：句子向量的的数据在svRuleInfo的rEntityWordInfosList里面和外面；其绑定的rule的数据，全部放在入参rRuleEntityMap里面
            String sentence = svRuleInfo.getSentence();
            List<String> words = svRuleInfo.getWords();
            List<String> natures = svRuleInfo.getNatures();
            List<Double> weights = svRuleInfo.getWeights();
            Integer sentenceVectorId = svRuleInfo.getSentenceVectorId();
            Integer ruleId = svRuleInfo.getRuleId();                                                                    //指向ruleId的这条rule
            String currentKey = sentenceVectorId + "_" + ruleId;

            List<List<REntityWordInfo>> rEntityWordInfosList = svRuleInfo.getrEntityWordInfosList();                    //代表了句子向量一方的数据
            int entitySize = rEntityWordInfosList.size();
            Map<String, RRuleEntity> rRuleEntityMap = ruleId_RRuleEntityDataMap.get(ruleId);                            //获取这条rule下面保存的所有RRE对象Map

            List<WordInfo> redundantWordInfos = new ArrayList<>();                                                       //多余的词语集合
            List<REntityWordInfo> matchedREntityWordInfos = new ArrayList<>();                                          //选择上的REW关系集合（不同分词模式选择的REW集合不同）
            List<RRuleEntity> matchedRRuleEntities = new ArrayList<>();                                                 //匹配上的RRE关系集合
            List<RRuleEntity> lackedRRuleEntities = new ArrayList<>();                                                  //没匹配上的RRE关系集合

            for (RRuleEntity rRuleEntity : rRuleEntityMap.values()) {
                if(rRuleEntity.getQueryType() != Constant.AB_NO_QUERY_MODE)                                          //如果询问模式不是“不必询问”，则表示是必须参数
                    lackedRRuleEntities.add(rRuleEntity);                                                               //先将此rule下面的所有必须参数都装进去，后面一个个去除
            }

            //2.给予每个实体集合的每个实体一次机会，如果能匹配上，则 句子向量和实体本身的两端 的 单项数量占比和单项权重占 比都被积累进入 交集数量占比值和交集权重占比值中
            Double similarity = 0d;
            Double intersectionVolumeRateOccupancy = 0d;
            Double intersectionWeightOccupancy = 0d;
            A:for(int i = 0; i < rEntityWordInfosList.size(); i ++){                                                          //遍历svRuleInfo绑定的一个分词方式指定的实体集群
                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosList.get(i);                                        //第i个word分词段归属到的实体集合
                B:for(int j = 0; j < rEntityWordInfos.size(); j ++){                                                              //遍历每一个分词段指定的实体集
                    REntityWordInfo rEntityWordInfo = rEntityWordInfos.get(j);
                    String entityTypeId = rEntityWordInfo.getEntityTypeId();
                    RRuleEntity rRuleEntity = rRuleEntityMap.get(entityTypeId);                                                  //按照这个key，检索到，就表示被记录，表示能匹配上
                    if(rRuleEntity != null){
                        //记录此句子向量中归属的实体成功匹配上绑定的规则中的一个实体了 //TODO: 那么规则中的rRuleEntity也可以在db中记录这一次匹配，可以统计一个rRuleEntity的匹配次数
//                        System.out.println("Matched！ : 原句" + sentence + ", 分词方式：" + Arrays.toString(words.toArray()) + JSON.toJSONString(rEntityWordInfo) + " --- " + JSON.toJSONString(rRuleEntity));

                        Double volumeRateItem = 1.0d / entitySize;                                                              //这一项在句子中的数量比重
                        Double volumeRate = rRuleEntity.getVolumeRate();                                                        //这一项在规则中的数量比重
                        intersectionVolumeRateOccupancy = intersectionVolumeRateOccupancy + volumeRateItem + volumeRate;                                //积累数量比重

                        Double sv_weight = weights.get(i);                                                                      //句子向量中，第i个word分词归属到的实体的权重
                        Double rule_weight = rRuleEntity.getWeight();                                                           //rule模板中，这个实体在rule中的权重
                        intersectionWeightOccupancy = intersectionWeightOccupancy + sv_weight + rule_weight;                    //积累权重比重

                        matchedREntityWordInfos.add(rEntityWordInfo);                                                           //此分词模式--规则绑定体中，匹配上的REW，加入到这个集合中
                        matchedRRuleEntities.add(rRuleEntity);                                                                  //将rRuleEntity添加到匹配上的 规则-实体关系 列表中
                        lackedRRuleEntities.remove(rRuleEntity);                                                                //匹配上的参数就不是缺失参数了

                        if(enableCalculateLog){
                            List<REntityWordInfo> rewis = ids_rewDatasMap.get(currentKey);                                        //TODO:这个日志里面还可以装入缺失实体相关信息，日志直接装REWI对象得了
                            if(rewis == null) rewis = new ArrayList<>();
                            rewis.add(rEntityWordInfo);
                            ids_rewDatasMap.put(currentKey, rewis);
                        }
                        break B;                                                                                               //一个ruleId下，只要一个词语的一个实体被匹配上即可，无需遍历所有此词语下的实体
                    }else{
                        String word = rEntityWordInfo.getWord();
                        Integer wordId = rEntityWordInfo.getWordId();
                        Double weight = rEntityWordInfo.getWeights().get(sentenceVectorId);
                        weight = (weight != null) ? weight : 0d;
                        WordInfo redundantWordInfo = new WordInfo(wordId, word, weight);
                        redundantWordInfos.add(redundantWordInfo);
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

            svRuleInfo.setRedundantWordInfos(redundantWordInfos);
            svRuleInfo.setMatchedREntityWordInfos(matchedREntityWordInfos);
            svRuleInfo.setMatchedRRuleEntities(matchedRRuleEntities);
            svRuleInfo.setLackedRRuleEntities(lackedRRuleEntities);

            if(enableCalculateLog){
                StringBuffer sb = new StringBuffer();
                sb.append(similarity).append(" = ").append(intersectionVolumeRateOccupancy != 0d ? intersectionVolumeRateOccupancy + " * " : "").append(intersectionWeightOccupancy != 0d ? intersectionWeightOccupancy : "");
                ids_scoreMap.put(currentKey, sb.toString());
                similarityCalculationData_fpm.setIds_rewisMap(ids_rewDatasMap);
                similarityCalculationData_fpm.setIds_scoreMap(ids_scoreMap);
            }
        }
        return svRuleInfos;
    }

    /**
     * 余弦相似度
     * @param svRuleInfos
     * @param ruleId_RRuleEntityDataMap
     * @return
     */
    private List<SVRuleInfo> cosineSimilarity_FPM(List<SVRuleInfo> svRuleInfos, Map<Integer, Map<String, RRuleEntity>> ruleId_RRuleEntityDataMap){
        for (SVRuleInfo svRuleInfo : svRuleInfos) {                                                                     //下面是3层循环，所以svRuleInfos在前面做了优化，限定数量最多为5个
            //1.准备好两方数据：句子向量的的数据在svRuleInfo的rEntityWordInfosList里面和外面；其绑定的rule的数据，全部放在入参rRuleEntityMap里面
            String sentence = svRuleInfo.getSentence();
            List<String> words = svRuleInfo.getWords();
            List<String> natures = svRuleInfo.getNatures();
            List<Double> weights = svRuleInfo.getWeights();

            List<List<REntityWordInfo>> rEntityWordInfosList = svRuleInfo.getrEntityWordInfosList();                    //代表了句子向量一方的数据

            Integer ruleId = svRuleInfo.getRuleId();                                                                    //指向ruleId的这条rule
            Map<String, RRuleEntity> rRuleEntityMap = ruleId_RRuleEntityDataMap.get(ruleId);                            //获取这条rule下面保存的所有RRE对象Map

            List<REntityWordInfo> matchedREntityWordInfos = new ArrayList<>();                                          //选择上的REW关系集合（不同分词模式选择的REW集合不同）
            List<RRuleEntity> matchedRRuleEntities = new ArrayList<>();                                                 //匹配上的RRE关系集合
            List<RRuleEntity> lackedRRuleEntities = new ArrayList<>();                                                  //没匹配上的RRE关系集合

            for (RRuleEntity rRuleEntity : rRuleEntityMap.values()) {
                if(rRuleEntity.getQueryType() != Constant.AB_NO_QUERY_MODE)                                          //如果询问模式不是“不必询问”，则表示是必须参数
                    lackedRRuleEntities.add(rRuleEntity);                                                               //先将此rule下面的所有必须参数都装进去，后面一个个去除
            }

            //2.给予每个实体集合的每个实体一次机会，如果能匹配上，则填充余弦公式的分子
            Double numerator = 0d;                                                                                      //分子
            Double sv_weight_square = 0d;                                                                               //句子向量中的权重平方和
            Double rule_weight_square = 0d;                                                                             //规则中的权重平方和
            Double cosineValue = 0d;                                                                                    //cosine值（由上面3个量计算得到）

            //3.构建分子
            for(int i = 0; i < rEntityWordInfosList.size(); i ++){                                                          //遍历svRuleInfo绑定的一个分词方式指定的实体集群
                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosList.get(i);                                        //第i个word分词段归属到的实体集合
                for(int j = 0; j < rEntityWordInfos.size(); j ++){                                                              //遍历每一个分词段指定的实体集
                    REntityWordInfo rEntityWordInfo = rEntityWordInfos.get(j);
                    String entityTypeId = rEntityWordInfo.getEntityTypeId();
                    RRuleEntity rRuleEntity = rRuleEntityMap.get(entityTypeId);                                                  //按照这个key，检索到，就表示被记录，表示能匹配上
                    if(rRuleEntity != null){
                        //记录此句子向量中归属的实体成功匹配上绑定的规则中的一个实体了 //TODO: 那么规则中的rRuleEntity也可以在db中记录这一次匹配，可以统计一个rRuleEntity的匹配次数
//                        System.out.println("Matched！ : 原句" + sentence + ", 分词方式：" + Arrays.toString(words.toArray()) + JSON.toJSONString(rEntityWordInfo) + " --- " + JSON.toJSONString(rRuleEntity));
                        Double sv_weight = weights.get(i);                                                                      //句子向量中，第i个word分词归属到的实体的权重
                        Double rule_weight = rRuleEntity.getWeight();                                                           //rule模板中，这个实体在rule中的权重
                        numerator += sv_weight * rule_weight;

                        matchedREntityWordInfos.add(rEntityWordInfo);                                                           //此分词模式--规则绑定体中，匹配上的REW，加入到这个集合中
                        matchedRRuleEntities.add(rRuleEntity);                                                                  //将rRuleEntity添加到匹配上的 规则-实体关系 列表中
                        lackedRRuleEntities.remove(rRuleEntity);                                                                //匹配上的参数就不是缺失参数了
                    }
                }
            }

            //4.构建分母的一个因子
            for (Double weight : weights) {
                sv_weight_square += weight * weight;
            }

            //5.构建分母的第二个因子
            Collection<RRuleEntity> rRuleEntities = rRuleEntityMap.values();
            for (RRuleEntity rRuleEntity : rRuleEntities) {
                if(rRuleEntity.getRuleId() == ruleId){
                    Double weight = rRuleEntity.getWeight();
                    rule_weight_square += weight * weight;
                }
            }
            cosineValue = numerator / (Math.sqrt(sv_weight_square) * Math.sqrt(rule_weight_square));
            svRuleInfo.setSimilarity(cosineValue);

            svRuleInfo.setMatchedREntityWordInfos(matchedREntityWordInfos);
            svRuleInfo.setMatchedRRuleEntities(matchedRRuleEntities);
            svRuleInfo.setLackedRRuleEntities(lackedRRuleEntities);
        }
        return svRuleInfos;
    }

    /**
     * 皮尔逊相似度
     * @param svRuleInfos
     * @param ruleId_RRuleEntityDataMap
     * @return
     */
    private List<SVRuleInfo> pearsonSimilarity_FPM(List<SVRuleInfo> svRuleInfos, Map<Integer, Map<String, RRuleEntity>>  ruleId_RRuleEntityDataMap){
        for (SVRuleInfo svRuleInfo : svRuleInfos) {                                                                     //下面是3层循环，所以svRuleInfos在前面做了优化，限定数量最多为5个
            //1.准备好两方数据：句子向量的的数据在svRuleInfo的rEntityWordInfosList里面和外面；其绑定的rule的数据，全部放在入参rRuleEntityMap里面
            String sentence = svRuleInfo.getSentence();
            List<String> words = svRuleInfo.getWords();
            List<String> natures = svRuleInfo.getNatures();
            List<Double> weights = svRuleInfo.getWeights();
            List<Double> weightsClone = new ArrayList<>();                                                              //weights的克隆对象集合
            weightsClone.addAll(weights);

            List<List<REntityWordInfo>> rEntityWordInfosList = svRuleInfo.getrEntityWordInfosList();                    //代表了句子向量一方的数据

            Integer ruleId = svRuleInfo.getRuleId();                                                                    //指向ruleId的这条rule
            Map<String, RRuleEntity> rRuleEntityMap = ruleId_RRuleEntityDataMap.get(ruleId);                            //获取这条rule下面保存的所有RRE对象Map

            List<REntityWordInfo> matchedREntityWordInfos = new ArrayList<>();                                          //选择上的REW关系集合（不同分词模式选择的REW集合不同）
            List<RRuleEntity> matchedRRuleEntities = new ArrayList<>();                                                 //匹配上的RRE关系集合
            List<RRuleEntity> lackedRRuleEntities = new ArrayList<>();                                                  //没匹配上的RRE关系集合

            for (RRuleEntity rRuleEntity : rRuleEntityMap.values()) {
                if(rRuleEntity.getQueryType() != Constant.AB_NO_QUERY_MODE)                                          //如果询问模式不是“不必询问”，则表示是必须参数
                    lackedRRuleEntities.add(rRuleEntity);                                                               //先将此rule下面的所有必须参数都装进去，后面一个个去除
            }

            //2.构建两个数组用来装载两个向量
            List<Double> sv_weights = new ArrayList<>();
            List<Double> rule_weights = new ArrayList<>();
            //2.1.装载交集部分
            for(int i = 0; i < rEntityWordInfosList.size(); i ++){                                                          //遍历svRuleInfo绑定的一个分词方式指定的实体集群
                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosList.get(i);                                        //第i个word分词段归属到的实体集合
                for(int j = 0; j < rEntityWordInfos.size(); j ++){                                                              //遍历每一个分词段指定的实体集
                    REntityWordInfo rEntityWordInfo = rEntityWordInfos.get(j);
                    String entityTypeId = rEntityWordInfo.getEntityTypeId();
                    RRuleEntity rRuleEntity = rRuleEntityMap.get(entityTypeId);                                                  //按照这个key，检索到，就表示被记录，表示能匹配上
                    if(rRuleEntity != null){
                        //记录此句子向量中归属的实体成功匹配上绑定的规则中的一个实体了 //TODO: 那么规则中的rRuleEntity也可以在db中记录这一次匹配，可以统计一个rRuleEntity的匹配次数
//                        System.out.println("Matched！ : 原句" + sentence + ", 分词方式：" + Arrays.toString(words.toArray()) + JSON.toJSONString(rEntityWordInfo) + " --- " + JSON.toJSONString(rRuleEntity));

                        Double sv_weight = weights.get(i);                                                                      //句子向量中，第i个word分词归属到的实体的权重
                        Double rule_weight = rRuleEntity.getWeight();                                                           //rule模板中，这个实体在rule中的权重
                        sv_weights.add(sv_weight);
                        rule_weights.add(rule_weight);
                        weightsClone.set(i, -1d);                                                                               //将第i个位置设置为-1，说明此位置已经匹配上了

                        matchedREntityWordInfos.add(rEntityWordInfo);                                                           //此分词模式--规则绑定体中，匹配上的REW，加入到这个集合中
                        matchedRRuleEntities.add(rRuleEntity);                                                                  //将rRuleEntity添加到匹配上的 规则-实体关系 列表中
                        lackedRRuleEntities.remove(rRuleEntity);                                                                //匹配上的参数就不是缺失参数了
                    }
                }
            }

            //2.2.将句子向量中没被匹配上的权重项取出来，装载句子向量端的非零部分数据
            for (Double weight : weightsClone) {
                if(weight != -1d){
                    sv_weights.add(weight);
                    rule_weights.add(0d);
                }
            }

            //2.3.将规则中没被匹配上的权重项取出来，装载规则端的非零部分数据
            List<RRuleEntity> rRuleEntitiesRecord = new ArrayList<>();
            rRuleEntitiesRecord.addAll(rRuleEntityMap.values());                                                        //rRuleEntityMap.values()自身只能读，不要让其改变，故而赋值给一个新的集合
            rRuleEntitiesRecord.removeAll(matchedRRuleEntities);                                                        //删掉匹配上了的RRuleEntity对象，剩下的就是规则中没有匹配上的（但还可能包含其他ruleId的RRE）
            for (RRuleEntity rRuleEntity : rRuleEntitiesRecord) {
                if(rRuleEntity.getRuleId() == ruleId){                                                                  //过滤掉其他ruleId的RRE
                    sv_weights.add(0d);
                    rule_weights.add(rRuleEntity.getWeight());
                }
            }
            //3.计算皮尔逊相似度值
            double pearsonSimilarity = AlgorithmUtil.pearson(sv_weights, rule_weights);
            svRuleInfo.setSimilarity(pearsonSimilarity);
            svRuleInfo.setMatchedREntityWordInfos(matchedREntityWordInfos);
            svRuleInfo.setMatchedRRuleEntities(matchedRRuleEntities);
            svRuleInfo.setLackedRRuleEntities(lackedRRuleEntities);
        }
        return svRuleInfos;
    }

//****************************************************************************************************************************缺参匹配算法
    /**
     * 缺参匹配下的Jaccard相似度算法
     * @param historySvRuleInfo
     * @param rRuleEntityMap
     * @param typeId JACCARD_VOLUME_RATE 只考虑实体数量占比率；JACCARD_WEIGHT_RATE 只考虑实体权重占比率； JACCARD_VOLUME_WEIGHT_RATE 考虑两则的乘积
     * @return
     */
    private SVRuleInfo jaccardSimilarity_LMP(SVRuleInfo historySvRuleInfo, Map<String, RRuleEntity> rRuleEntityMap, int typeId, SimilarityCalculationData_LPM similarityCalculationData_lpm) {
        //1.准备好两方数据：句子向量的的数据在svRuleInfo的matchedREntityWordInfos；其绑定的rule的数据，全部放在入参rRuleEntityMap里面
        Integer sentenceVectorId = historySvRuleInfo.getSentenceVectorId();
        String sentence = historySvRuleInfo.getSentence();
        List<REntityWordInfo> matchedREntityWordInfos = historySvRuleInfo.getMatchedREntityWordInfos();                 //选择上的REW关系集合（不同分词模式选择的REW集合不同）
        int sectionSize = matchedREntityWordInfos.size();
        if(similarityCalculationData_lpm != null){                                                                          //*********************准备缺参日志记录对象
            List<String> intersectionEntityVolumnRates = similarityCalculationData_lpm.getIntersectionEntityVolumnRates();
            List<String> intersectionEntityWeightRates = similarityCalculationData_lpm.getIntersectionEntityWeightRates();
            if(intersectionEntityVolumnRates == null){
                similarityCalculationData_lpm.setIntersectionEntityVolumnRates(new ArrayList<String>());
            }
            if(intersectionEntityWeightRates == null){
                similarityCalculationData_lpm.setIntersectionEntityWeightRates(new ArrayList<String>());
            }
        }


        //2.给予每个实体集合的每个实体一次机会，如果能匹配上，则 句子向量和规则实体本身的两端 的 单项数量占比和单项权重占 比都被积累进入 交集数量占比值和交集权重占比值中
        Double similarity = 0d;
        Double intersectionVolumeRateOccupancy = 0d;
        Double intersectionWeightOccupancy = 0d;
        for(int i = 0; i < matchedREntityWordInfos.size(); i ++){                                                      //遍历每一个分词段指定的实体集
            REntityWordInfo rEntityWordInfo = matchedREntityWordInfos.get(i);                                           //获取句子端REW

            String entityTypeId = rEntityWordInfo.getEntityTypeId();                                                    //获取关联的entityTypeId
            RRuleEntity rRuleEntity = rRuleEntityMap.get(entityTypeId);                                                 //按照这个entityTypeId，检索到，就表示被记录，表示能匹配上
            if(rRuleEntity != null){
                //记录此句子向量中归属的实体成功匹配上绑定的规则中的一个实体了 //TODO: 那么规则中的rRuleEntity也可以在db中记录这一次匹配，可以统计一个rRuleEntity的匹配次数
//                System.out.println("Matched！ : 原句" + sentence + ",补充后：" + JSON.toJSONString(rEntityWordInfo) + " --- " + JSON.toJSONString(rRuleEntity));

                Double sv_volumeRate = 1.0d / sectionSize;                                                              //这一项在句子中的数量比重
                Double rule_volumeRate = rRuleEntity.getVolumeRate();                                                        //这一项在规则中的数量比重
                intersectionVolumeRateOccupancy = intersectionVolumeRateOccupancy + sv_volumeRate + rule_volumeRate;        //积累数量比重

                List<Double> weights = rEntityWordInfo.getWeights();
                Double sv_weightRate = weights.get(sentenceVectorId);                                                     //句子向量中，分词归属到的实体的权重
                Double rule_weightRate = rRuleEntity.getWeight();                                                           //rule模板中，这个实体在rule中的权重
                intersectionWeightOccupancy = intersectionWeightOccupancy + sv_weightRate + rule_weightRate;                    //积累权重比重

                if(similarityCalculationData_lpm != null){                                                                  //*************************算法内部日志准备：此对象不为null，即表示需要收集日志
                    List<String> intersectionEntityVolumnRates = similarityCalculationData_lpm.getIntersectionEntityVolumnRates();
                    List<String> intersectionEntityWeightRates = similarityCalculationData_lpm.getIntersectionEntityWeightRates();
                    StringBuffer volumnRateSB = new StringBuffer();
                    StringBuffer weightRateSB = new StringBuffer();
                    volumnRateSB.append("( ").append(rRuleEntity.getEntityId()).append("_").append(rRuleEntity.getEntityName()).append(" )_").append(sv_volumeRate).append("_").append(rule_volumeRate);
                    weightRateSB.append("( ").append(rRuleEntity.getEntityId()).append("_").append(rRuleEntity.getEntityName()).append(" )_").append(sv_weightRate).append("_").append(rule_weightRate);
                    intersectionEntityVolumnRates.add(volumnRateSB.toString());
                    intersectionEntityWeightRates.add(weightRateSB.toString());
                }

            }
        }
        intersectionVolumeRateOccupancy /= 2;                                                                           //只求交集，无论数量还是权重，全集肯定是2
        intersectionWeightOccupancy /= 2;

        switch (typeId){
            case Constant.JACCARD_VOLUME_RATE : {
                similarity = intersectionVolumeRateOccupancy;
                if(similarityCalculationData_lpm != null){                                                                  //*************************算法内部日志准备
                    similarityCalculationData_lpm.setSimilarityValue(similarity + " --> " + intersectionVolumeRateOccupancy);
                }
                break;
            }
            case Constant.JACCARD_WEIGHT_RATE : {
                similarity = intersectionWeightOccupancy;
                if(similarityCalculationData_lpm != null){                                                                  //*************************算法内部日志准备
                    similarityCalculationData_lpm.setSimilarityValue(similarity + " --> " + intersectionWeightOccupancy);
                }
                break;
            }
            case Constant.JACCARD_VOLUME_WEIGHT_RATE : {
                similarity = intersectionVolumeRateOccupancy * intersectionWeightOccupancy;
                if(similarityCalculationData_lpm != null){                                                                  //*************************算法内部日志准备
                    similarityCalculationData_lpm.setSimilarityValue(similarity + " --> " + intersectionVolumeRateOccupancy + " * " + intersectionWeightOccupancy);
                }
                break;
            }
            default:{
                similarity = intersectionVolumeRateOccupancy * intersectionWeightOccupancy;
                if(similarityCalculationData_lpm != null){                                                                  //*************************算法内部日志准备
                    similarityCalculationData_lpm.setSimilarityValue(similarity + " --> " + intersectionVolumeRateOccupancy + " * " + intersectionWeightOccupancy);
                }
            }
        }
        historySvRuleInfo.setSimilarity(similarity);                                                                    //缺参匹配从新计算的相似度

        return historySvRuleInfo;
    }

    /**
     * 缺参匹配下的余弦相似度算法
     * @param historySvRuleInfo
     * @param rRuleEntityMap
     * @return
     */
    private SVRuleInfo cosineSimilarity_LMP(SVRuleInfo historySvRuleInfo, Map<String, RRuleEntity> rRuleEntityMap) {
        //1.准备好两方数据：句子向量的的数据在svRuleInfo的rEntityWordInfosList里面和外面；其绑定的rule的数据，全部放在入参rRuleEntityMap里面
        Integer sentenceVectorId = historySvRuleInfo.getSentenceVectorId();
        String sentence = historySvRuleInfo.getSentence();
        List<String> words = historySvRuleInfo.getWords();
        List<Double> weights = historySvRuleInfo.getWeights();
        Integer ruleId = historySvRuleInfo.getRuleId();                                                                 //指向ruleId的这条rule
        List<REntityWordInfo> matchedREntityWordInfos = historySvRuleInfo.getMatchedREntityWordInfos();                 //选择上的REW关系集合（不同分词模式选择的REW集合不同）

        //2.给予每个实体集合的每个实体一次机会，如果能匹配上，则填充余弦公式的分子
        Double numerator = 0d;                                                                                          //分子
        Double sv_weight_square = 0d;                                                                                   //句子向量中的权重平方和
        Double rule_weight_square = 0d;                                                                                 //规则中的权重平方和
        Double cosineValue = 0d;                                                                                        //cosine值（由上面3个量计算得到）

        //3.构建分子
        for(int i = 0; i < matchedREntityWordInfos.size(); i ++){                                                      //遍历每一个分词段指定的实体集
            REntityWordInfo rEntityWordInfo = matchedREntityWordInfos.get(i);
            String entityTypeId = rEntityWordInfo.getEntityTypeId();
            RRuleEntity rRuleEntity = rRuleEntityMap.get(entityTypeId);                                                 //按照这个key，检索到，就表示被记录，表示能匹配上
            if(rRuleEntity != null){
                //记录此句子向量中归属的实体成功匹配上绑定的规则中的一个实体了 //TODO: 那么规则中的rRuleEntity也可以在db中记录这一次匹配，可以统计一个rRuleEntity的匹配次数
//                System.out.println("Matched！ : 原句" + sentence + ", 分词方式：" + Arrays.toString(words.toArray()) + JSON.toJSONString(rEntityWordInfo) + " --- " + JSON.toJSONString(rRuleEntity));
                List<Double> rEWIWeights = rEntityWordInfo.getWeights();
                Double sv_weight = rEWIWeights.get(sentenceVectorId);                                                     //句子向量中，分词归属到的实体的权重
                Double rule_weight = rRuleEntity.getWeight();                                                           //rule模板中，这个实体在rule中的权重
                numerator += sv_weight * rule_weight;
            }
        }

        //4.构建分母的一个因子
        for (Double weight : weights) {
            sv_weight_square += weight * weight;
        }

        //5.构建分母的第二个因子
        Collection<RRuleEntity> rRuleEntities = rRuleEntityMap.values();
        for (RRuleEntity rRuleEntity : rRuleEntities) {
            if(rRuleEntity.getRuleId() == ruleId){
                Double weight = rRuleEntity.getWeight();
                rule_weight_square += weight * weight;
            }
        }
        cosineValue = numerator / (Math.sqrt(sv_weight_square) * Math.sqrt(rule_weight_square));
        historySvRuleInfo.setSimilarity(cosineValue);

        return historySvRuleInfo;
    }

    /**
     * 缺参匹配下的皮尔逊相似度算法
     * @param historySvRuleInfo
     * @param rRuleEntityMap
     * @return
     */
    private SVRuleInfo pearsonSimilarity_LMP(SVRuleInfo historySvRuleInfo, Map<String, RRuleEntity> rRuleEntityMap) {
        //1.准备好两方数据：句子向量的的数据在svRuleInfo的rEntityWordInfosList里面和外面；其绑定的rule的数据，全部放在入参rRuleEntityMap里面
        Integer sentenceVectorId = historySvRuleInfo.getSentenceVectorId();
        String sentence = historySvRuleInfo.getSentence();
        List<String> words = historySvRuleInfo.getWords();
        List<String> natures = historySvRuleInfo.getNatures();
        List<Double> weights = historySvRuleInfo.getWeights();
        List<Double> weightsClone = new ArrayList<>();                                                                  //weights的克隆对象集合
        weightsClone.addAll(weights);
        List<REntityWordInfo> matchedREntityWordInfos = historySvRuleInfo.getMatchedREntityWordInfos();                 //本分词模式下的，选择上的REW关系集合
        List<RRuleEntity> matchedRRuleEntities = historySvRuleInfo.getMatchedRRuleEntities();                           //本分词模式下的，匹配上的RRE关系集合

        //2.构建两个数组用来装载两个向量
        List<Double> sv_weights = new ArrayList<>();
        List<Double> rule_weights = new ArrayList<>();

        //2.1.装载交集部分
        for(int i = 0; i < matchedREntityWordInfos.size(); i ++){                                                      //遍历每一个分词段指定的实体集
            REntityWordInfo rEntityWordInfo = matchedREntityWordInfos.get(i);
            String entityTypeId = rEntityWordInfo.getEntityTypeId();
            RRuleEntity rRuleEntity = rRuleEntityMap.get(entityTypeId);                                                 //按照这个key，检索到，就表示被记录，表示能匹配上
            if(rRuleEntity != null){
                //记录此句子向量中归属的实体成功匹配上绑定的规则中的一个实体了 //TODO: 那么规则中的rRuleEntity也可以在db中记录这一次匹配，可以统计一个rRuleEntity的匹配次数
//                System.out.println("Matched！ : 原句" + sentence + ", 分词方式：" + Arrays.toString(words.toArray()) + JSON.toJSONString(rEntityWordInfo) + " --- " + JSON.toJSONString(rRuleEntity));
                List<Double> rEWIWeights = rEntityWordInfo.getWeights();
                Double sv_weight = rEWIWeights.get(sentenceVectorId);                                                     //句子向量中，分词归属到的实体的权重
                Double rule_weight = rRuleEntity.getWeight();                                                           //rule模板中，这个实体在rule中的权重
                sv_weights.add(sv_weight);
                rule_weights.add(rule_weight);
                weightsClone.set(i, -1d);                                                                               //将第i个位置设置为-1，说明此位置已经匹配上了
            }
        }

        //2.2.将句子向量中没被匹配上的权重项取出来，装载句子向量端的非零部分数据
        for (Double weight : weightsClone) {
            if(weight != -1d){
                sv_weights.add(weight);
                rule_weights.add(0d);
            }
        }

        //2.3.将规则中没被匹配上的权重项取出来，装载规则端的非零部分数据
        List<RRuleEntity> rRuleEntitiesRecord = new ArrayList<>();
        rRuleEntitiesRecord.addAll(rRuleEntityMap.values());                                                            //rRuleEntityMap.values()自身只能读，不要让其改变，故而赋值给一个新的集合
        rRuleEntitiesRecord.removeAll(matchedRRuleEntities);                                                            //删掉匹配上了的RRuleEntity对象，剩下的就是规则中没有匹配上的（但还可能包含其他ruleId的RRE）
        for (RRuleEntity rRuleEntity : rRuleEntitiesRecord) {
            sv_weights.add(0d);
            rule_weights.add(rRuleEntity.getWeight());
        }

        //3.计算皮尔逊相似度值
        double pearsonSimilarity = AlgorithmUtil.pearson(sv_weights, rule_weights);
        historySvRuleInfo.setSimilarity(pearsonSimilarity);
        return historySvRuleInfo;
    }
}
