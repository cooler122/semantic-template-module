package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponent;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.*;

@Component("changeParamMatchComponent")
public class ChangeParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(ChangeParamMatchComponentImpl.class.getName());

    public ChangeParamMatchComponentImpl() {
        super("CPMC", "sentenceVectors", "optimalSvRuleInfo_CPM");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.trace("CPMC.换参匹配");

        DataComponent<SemanticParserRequest> dataComponent = componentConstant.getDataComponent("semanticParserRequest", contextOwner);
        SemanticParserRequest request = dataComponent.getData();
        int calculationLogType = request.getCalculationLogType();

        //1.数据准备
        DataComponent<List<DataComponent<SVRuleInfo>>> historyDataComponent = componentConstant.getDataComponent("historyDataComponents", contextOwner);
        List<DataComponent<SVRuleInfo>> historyDataComponents = historyDataComponent.getData();
        Integer currentContextId = contextOwner.getContextId();
        Map<Integer, SVRuleInfo> contextId_svRuleInfoMap = new HashMap<>();                                             //用来记录历史上下文数据，形式为：Map<historyContextId, historyOptimalSVRuleInfo>
        Map<Integer, Double> historyVolumeIncrementMap = new HashMap<>();                                               //记录每一轮历史对话的匹配上的REWI集合的各个比重增量的Map<contextId, 1/REWIs' size>
        Map<String, List<REntityWordInfo>> historyREWIMap = new HashMap<>();                                            //将每一轮的REWI放入Map<entityTypeId, List<REWI>>

        for(DataComponent<SVRuleInfo> historyData : historyDataComponents){                                             //遍历查询出来的这些历史数据
            if(historyData != null && historyData.getData() != null){
                Integer historyContextId = historyData.getContextOwner().getContextId();
                SVRuleInfo svRuleInfo = historyData.getData();
                contextId_svRuleInfoMap.put(historyContextId, svRuleInfo);                                              //收集此上下文数据
//                if(svRuleInfo.getLackedRRuleEntities() == null || svRuleInfo.getLackedRRuleEntities().size() == 0){   //TODO:如果只保证历史状态为全参状态才能换参匹配，那么就要解开此注释，但本人思考，缺参状态也应该换参，保证新入实体信息给接收，这个还是根据后续效果来定吧
                List<REntityWordInfo> matchedREntityWordInfos = svRuleInfo.getMatchedREntityWordInfos();                //取出已经匹配过的历史REWI集合记录
                int historyMatchedREWISize = matchedREntityWordInfos.size() ;                                           //获取并收集每轮历史对话匹配集长度，后续用来计算个数占比
                if(historyMatchedREWISize > 0){
                    historyVolumeIncrementMap.put(historyContextId, 1d / historyMatchedREWISize);
                    for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {                            //将匹配上的历史REW集合放入一个Map中
                        String entityTypeId = matchedREntityWordInfo.getEntityTypeId();
                        List<REntityWordInfo> rEntityWordInfos = historyREWIMap.get(entityTypeId);
                        if(rEntityWordInfos == null){
                            rEntityWordInfos = new ArrayList<>();
                        }
                        rEntityWordInfos.add(matchedREntityWordInfo);
                        historyREWIMap.put(entityTypeId, rEntityWordInfos);                                             //将历史REWI保存到historyREWIMap
                    }
                }
//                }
            }
        }

        if(contextId_svRuleInfoMap.size() == 0) return new ComponentBizResult("CPMC_F");                 //如果没有收集到历史规则，就跳出去吧

        Map<String, REntityWordInfo> hitCurrentREntityWordInfoMap = new HashMap<>();                                    //关联数据Map<{sentenceVectorId, currentEntityType, currentEntityId}, hitCurrentREWI>
        Map<String, Double> svIdcontextId_productValueMap = new HashMap<>();                                            //统计值Map<sentenceVectorId_contextId, 统计数据值>
        Integer maxValueSentenceVectorId = null;                                                                        //最高匹配值的sentenceVectorId（和下面的contextId绑定）
        Integer maxValueContextId = null;                                                                               //最高匹配值的ContextId
        Double maxValue = 0d;                                                                                           //最高匹配值（此值是匹配上的实体的(1d / currentSVSize * currentWeight) * 3 + (volumeIncrement * historyWeight)的和值）
        Map<Integer, Map<String, WordInfo>> redundantWordInfoMaps = new HashMap<>();                                    //准备好一个多余词语大Map放入每一种分词方式下的小Map

        //2.计算最佳上下文：准备好关联数据，选好最高得分的contextId，确定换参匹配，匹配哪个历史上下文
        for (SentenceVector sentenceVector : sentenceVectors) {                                                         //句子端：查询本次检索出来的句子向量
            Integer sentenceVectorId = sentenceVector.getId();                                                          //句子向量ID
            List<List<REntityWordInfo>> currentREntityWordInfosList = sentenceVector.getrEntityWordInfosList();         //遍历当前句子向量里面的各个REWIs
            int currentSVSize = currentREntityWordInfosList.size();                                                     //句子向量长度

            for (List<REntityWordInfo> currentREntityWordInfos : currentREntityWordInfosList) {
                Map<String, WordInfo> redundantWordInfoMap = new HashMap<>();
                for (REntityWordInfo currentREntityWordInfo : currentREntityWordInfos) {
                    Integer currentEntityType = currentREntityWordInfo.getEntityType();
                    Integer currentEntityId = currentREntityWordInfo.getEntityId();
                    String currentEntityTypeId = currentREntityWordInfo.getEntityTypeId();                              //获取REWI的entityTypeId
                    Integer currentWordId = currentREntityWordInfo.getWordId();
                    String currentWord = currentREntityWordInfo.getWord();
                    Double currentWeight = currentREntityWordInfo.getWeights().get(sentenceVectorId);                 //获取本句子向量的此REWI的权重
                    currentWeight = (currentWeight != null) ? currentWeight : 0d;
                    redundantWordInfoMap.put(currentWord, new WordInfo(currentWordId, currentWord, currentWeight));                          //对于每一种分词类型下的每一个分词段，都放到小多余词语Map里面

                    List<REntityWordInfo> historyREntityWordInfos = historyREWIMap.get(currentEntityTypeId);            //历史端：尝试搜索此entityTypeId是否存在于历史匹配的REWI集合里面
                    if(historyREntityWordInfos != null && historyREntityWordInfos.size() > 0){                          //如果本次entityTypeId的REWI碰上了历史REWI集合
                        for (REntityWordInfo historyREntityWordInfo : historyREntityWordInfos) {
                            //1.获取此historyREWI的相关数据，对其值累计到统计数据中
                            Integer historyWordId = historyREntityWordInfo.getWordId();

                            if(historyWordId.intValue() != currentWordId.intValue()){                                   //如果文字词语都一样，那就无需换参了
                                //2.先将获得的能匹配上的 currentREntityWordInfo 对象放到关联Map中
                                hitCurrentREntityWordInfoMap.put(sentenceVectorId + "_" + currentEntityType + "_" + currentEntityId, currentREntityWordInfo);

                                Integer contextId = historyREntityWordInfo.getContextId();                                  //这个小历史REWI集合虽然共有一个相同的entityTypeId，但有不同的contextId，即会话版本不同
                                Double historyVolumeIncrement = historyVolumeIncrementMap.get(contextId);                          //获得此会话的数量单元增量
                                historyVolumeIncrement = historyVolumeIncrement != null ? historyVolumeIncrement : 0d;                           //volumeIncrement确保有值
                                List<Double> weights = historyREntityWordInfo.getWeights();
                                Double historyWeight = weights.get(0);                                     //获得此实体在这个会话里面的权重
                                historyWeight = historyWeight != null ? historyWeight : 0d;                                 //排除historyWeight为null的情况

                                double productValueIncrement = (1d / currentSVSize * currentWeight) * 3 + (historyVolumeIncrement * historyWeight);//当前句子REWI积值*3 和历史REWI积值 之和
//此处很关键                            System.out.println(currentContextId + " ---------- " + contextId + "   historyREWI ： " + historyREntityWordInfo.getEntityName() + " ----->  (1d / " + currentSVSize + " * " + currentWeight + ") * 3" + " + ( " + historyVolumeIncrement + " * " + historyWeight + " )");
                                double currentEntityWeightRateIncrement = 1d / currentSVSize * currentWeight;                               //此处只记录句子向量端的 量比重 * 权重比重

                                Double productValue = svIdcontextId_productValueMap.get(sentenceVectorId + "_" + contextId);
                                productValue = (productValue != null ? productValue : 0d) + productValueIncrement;          //将上面的统计数据体放到统计数据Map中

                                Double currentEntityWeightRate = svIdcontextId_productValueMap.get(sentenceVectorId + "_" + contextId + "_currentEntityWeight");
                                currentEntityWeightRate = (currentEntityWeightRate != null ? currentEntityWeightRate : 0d) + currentEntityWeightRateIncrement;

                                svIdcontextId_productValueMap.put(sentenceVectorId + "_" + contextId, productValue);        //两个值都放到Map中，第一个值作为比较标准，找到最优SVRuleInfo，第二个值作为是否进入全参匹配过程的参考值
                                svIdcontextId_productValueMap.put(sentenceVectorId + "_" + contextId + "_currentEntityWeight", currentEntityWeightRate);

                                if(productValue > maxValue || (productValue == maxValue && contextId > maxValueContextId)){ //比较，更新当前最大值的sentenceVectorId、contextId、maxValue（最终会找到最佳svId、contextId匹配结果）
                                    maxValueSentenceVectorId = sentenceVectorId;
                                    maxValueContextId = contextId;
                                    maxValue = productValue;
                                }
                            }
                        }
                    }
                }
                redundantWordInfoMaps.put(sentenceVectorId, redundantWordInfoMap);
            }
        }

        CalculationLogParam_CPM calculationLogParam_cpm = null;
        if(calculationLogType != Constant.NO_CALCULATION_LOG) {
            calculationLogParam_cpm = new CalculationLogParam_CPM();
            calculationLogParam_cpm.setSentenceVectors(sentenceVectors);
            calculationLogParam_cpm.setHistorySVRuleInfoMap(contextId_svRuleInfoMap);
            calculationLogParam_cpm.setHitCurrentREntityWordInfoMap(hitCurrentREntityWordInfoMap);
            calculationLogParam_cpm.setSvIdcontextId_productValueMap(svIdcontextId_productValueMap);
            calculationLogParam_cpm.setMaxValueSentenceVectorId(maxValueSentenceVectorId);
            calculationLogParam_cpm.setMaxValueContextId(maxValueContextId);
            calculationLogParam_cpm.setMaxValue(maxValue);
        }

        if(maxValueContextId != null && maxValueSentenceVectorId != null){
            //3.换参，设置历史规则为当前匹配规则：设置changeParamOptimalSvRuleInfo，并修改里面替换的参数，包括words和matchedREntityWordInfos
            SVRuleInfo bestHistoryOptimalSvRuleInfo = contextId_svRuleInfoMap.get(maxValueContextId);                   //这里通过maxValueContextId获取的SVRuleInfo对象作为换参匹配返回的changeParamOptimalSvRuleInfo，有待后面对比全参匹配进行选择
            String bestHistoryOptimalSvRuleInfoStr = JSON.toJSONString(bestHistoryOptimalSvRuleInfo);
            SVRuleInfo changeParamOptimalSvRuleInfo = JSON.parseObject(bestHistoryOptimalSvRuleInfoStr, SVRuleInfo.class);  //克隆一个全新的换参匹配SVRuleInfo对象，作为最优换参结果，后面将参数改一下（主要是contextId参数）
            changeParamOptimalSvRuleInfo.setMatchType(Constant.CPM);                                                   //设置匹配类型
            changeParamOptimalSvRuleInfo.setSentenceVectorId(maxValueSentenceVectorId);
            changeParamOptimalSvRuleInfo.setrEntityWordInfosList(sentenceVectors.get(maxValueSentenceVectorId).getrEntityWordInfosList());
//            changeParamOptimalSvRuleInfo.setAlgorithmType(?);        //这个只从历史记录里面取的，以前是什么，现在还是什么，无需设置。
            List<String> words = changeParamOptimalSvRuleInfo.getWords();
            List<REntityWordInfo> matchedREntityWordInfosModified = new ArrayList<>();                                  //准备一个新的REWI集合，作为换参SVRuleInfo的匹配REWI集合
            List<REntityWordInfo> matchedREntityWordInfos = changeParamOptimalSvRuleInfo.getMatchedREntityWordInfos();             //获取旧的REWI集合
            Map<String, WordInfo> maxValueRedundantWordInfoMap = redundantWordInfoMaps.get(maxValueSentenceVectorId);   //最佳maxValueSentenceVectorId下的redundantWordInfoMap
            for(int i = 0; i < matchedREntityWordInfos.size(); i ++){
                REntityWordInfo matchedREntityWordInfo = matchedREntityWordInfos.get(i);
                Integer entityType = matchedREntityWordInfo.getEntityType();
                Integer entityId = matchedREntityWordInfo.getEntityId();
                Double historyWeight = matchedREntityWordInfo.getWeights().get(0);
                REntityWordInfo hitCurrentREntityWordInfo = hitCurrentREntityWordInfoMap.get(maxValueSentenceVectorId + "_" + entityType + "_" + entityId);
                if(hitCurrentREntityWordInfo != null){
                    hitCurrentREntityWordInfo.setContextId(currentContextId);
                    hitCurrentREntityWordInfo.setWeights(Arrays.asList(historyWeight));                                 //注意权重还是要用历史规则里面REWI里面的权重，设置到第0位
                    matchedREntityWordInfosModified.add(hitCurrentREntityWordInfo);
                    words.set(i, hitCurrentREntityWordInfo.getWord());

                    maxValueRedundantWordInfoMap.remove(hitCurrentREntityWordInfo.getWord());                           //匹配上了，就将此词语从此Map中去掉
                }else{
                    matchedREntityWordInfo.setContextId(currentContextId);

                    Double historyMaxValueSentenceVectorWeight = matchedREntityWordInfo.getWeights().get(0);
                    matchedREntityWordInfo.setWeights(Arrays.asList(historyMaxValueSentenceVectorWeight));                     //一旦确认了最佳svId，就只设置它一个的weight了

                    matchedREntityWordInfosModified.add(matchedREntityWordInfo);
                }
            }
            changeParamOptimalSvRuleInfo.setMatchedREntityWordInfos(matchedREntityWordInfosModified);                   //重新设置修改的REWI集合
            List<WordInfo> redundantWordInfos = new ArrayList<>();
            redundantWordInfos.addAll(maxValueRedundantWordInfoMap.values());
            changeParamOptimalSvRuleInfo.setRedundantWordInfos(redundantWordInfos);                                     //设置多余词语

            List<String> wordsModified = new ArrayList<>();
            StringBuffer wordSB = new StringBuffer();
            for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfosModified) {
                String word = matchedREntityWordInfo.getWord();
                wordsModified.add(word);
                wordSB.append(word);
            }
            changeParamOptimalSvRuleInfo.setWords(wordsModified);                                                       //设置新的分词集合
            changeParamOptimalSvRuleInfo.setSentenceModified(wordSB.toString());                                        //设置新的修改句子

            //4.计算相关判断标准，确定换参结果是否是最终确定性结果：看是否changeParamOptimalSvRuleInfo，作为最终optimalSvRuleInfo，如果确定是，则无需走全参匹配过程，反之则走
            int historyEntitiesCount = 0;                                                                               //历史规则里的实体数量
            int currentEntitiesCount = 0;                                                                               //当前句子向量里的实体数量
            int currentCoreEntitiesCount = 0;                                                                           //当前核心实体数
            double currentHitEntityWeightRate = 0d;                                                                    //可换参数所占的权重在句子向量中权重的占比
            historyEntitiesCount = matchedREntityWordInfos.size();
            for (SentenceVector sentenceVector : sentenceVectors) {
                Integer sentenceVectorId = sentenceVector.getId();
                if(sentenceVectorId.intValue() == maxValueSentenceVectorId.intValue()){
                    List<String> natures = sentenceVector.getNatures();
                    currentEntitiesCount = natures.size();
                    for (String nature : natures) {
                        if(nature.startsWith("n")){
                            currentCoreEntitiesCount ++;
                        }
                    }
                    break;
                }
            }
            currentHitEntityWeightRate = svIdcontextId_productValueMap.get(maxValueSentenceVectorId + "_" + maxValueContextId + "_currentEntityWeight");
            if(historyEntitiesCount <= currentEntitiesCount || currentCoreEntitiesCount > 2 || currentHitEntityWeightRate < 0.6d){  //有这3个条件之一的，我心里就不踏实了，就强制它走一遍全参匹配
                changeParamOptimalSvRuleInfo.setEnsureFinal(false);
            }else{
                changeParamOptimalSvRuleInfo.setEnsureFinal(true);
            }
            if(calculationLogType != Constant.NO_CALCULATION_LOG){
                calculationLogParam_cpm.setChangeParamOptimalSvRuleInfo(changeParamOptimalSvRuleInfo);
                calculationLogParam_cpm.setHistoryEntitiesCount(historyEntitiesCount);
                calculationLogParam_cpm.setCurrentEntitiesCount(currentEntitiesCount);
                calculationLogParam_cpm.setCurrentCoreEntitiesCount(currentCoreEntitiesCount);
                calculationLogParam_cpm.setCurrentHitEntityWeightRate(currentHitEntityWeightRate);
                componentConstant.putDataComponent(new DataComponentBase("CalculationLogParam_CPM", contextOwner, "String", JSON.toJSONString(calculationLogParam_cpm)));     //保存缺参匹配的计算型日志
            }

            return new ComponentBizResult("CPMC_S", Constant.STORE_LOCAL_REMOTE, changeParamOptimalSvRuleInfo);   //此结果在本地和远程都要存储
        }else{
            System.out.println(JSON.toJSONString(calculationLogParam_cpm));
            return new ComponentBizResult("CPMC_F");
        }
    }
}
