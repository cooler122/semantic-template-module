package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.model.*;
import com.cooler.semantic.service.external.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component("changeParamMatchComponent")
public class ChangeParamMatchComponentImpl extends FunctionComponentBase<List<SentenceVector>, Object> {

    private static Logger logger = LoggerFactory.getLogger(ChangeParamMatchComponentImpl.class.getName());
    @Autowired
    private RedisService<SVRuleInfo> redisService;

    public ChangeParamMatchComponentImpl() {
        super("CPMC", "sentenceVectors", "optimalSvRuleInfo");
    }

    @Override
    protected ComponentBizResult<Object> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.debug("换参匹配");

        Integer currentContextId = contextOwner.getContextId();
        //1.准备好5轮的历史数据
        Map<Integer, SVRuleInfo> contextId_svRuleInfoMap = new HashMap<>();                                             //用来记录历史上下文数据
        Map<String, List<REntityWordInfo>> historyREWIMap = new HashMap<>();                                            //将每一轮的REWI放入Map<entityTypeId, List<REWI>>
        Map<Integer, Double> historyVolumeIncrementMap = new HashMap<>();                                               //记录每一轮历史对话的匹配上的REWI集合的各个权重的Map<contextId, 1/REWIs' size>
        for(int i = 1; i <= 5; i ++){                                                                                   //先查询出5轮历史数据（实际上可能2轮就足够了）
            String lastI_OwnerIndex = contextOwner.getLastNOwnerIndex(i);
            Integer lastIContextId = contextOwner.getLastNContextId(i);
            DataComponentBase<SVRuleInfo> historyData = redisService.getCacheObject(lastI_OwnerIndex + "_" + "optimalSvRuleInfo");//TODO：以后看看能否5次放到一起查出来
            if(historyData != null && historyData.getData() != null){
                SVRuleInfo svRuleInfo = historyData.getData();
                contextId_svRuleInfoMap.put(lastIContextId, svRuleInfo);                                                //收集此上下文数据
//                if(svRuleInfo.getLackedRRuleEntities() == null || svRuleInfo.getLackedRRuleEntities().size() == 0){   //TODO:如果只保证历史状态为全参状态才能换参匹配，那么就要解开此注释，但本人思考，缺参状态也应该换参，保证新入实体信息给接收，这个还是根据后续效果来定吧
                List<REntityWordInfo> matchedREntityWordInfos = svRuleInfo.getMatchedREntityWordInfos();                //取出已经匹配过的历史REWI集合记录

                int historyMatchedREWISize = matchedREntityWordInfos.size() ;                                           //获取并收集每轮历史对话匹配集长度，后续用来计算个数占比
                if(historyMatchedREWISize > 0){
                    historyVolumeIncrementMap.put(lastIContextId, 1d / historyMatchedREWISize);
                    for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {                            //将匹配上的历史REW集合放入一个Map中
                        String entityTypeId = matchedREntityWordInfo.getEntityTypeId();
                        List<REntityWordInfo> rEntityWordInfos = historyREWIMap.get(entityTypeId);
                        if(rEntityWordInfos == null){
                            rEntityWordInfos = new ArrayList<>();
                        }
                        rEntityWordInfos.add(matchedREntityWordInfo);
                        historyREWIMap.put(entityTypeId, rEntityWordInfos);
                    }
                }
//                }
            }
        }

        if(contextId_svRuleInfoMap.size() == 0) return new ComponentBizResult("CPMC_F");                 //如果没有收集到历史规则，就跳出去吧
        if(currentContextId == 15){
            System.out.println(JSON.toJSONString(contextId_svRuleInfoMap));
        }

        Map<CoordinateKey, REntityWordInfo> hitCurrentREntityWordInfoMap = new HashMap<>();                             //关联数据Map<{sentenceVectorId, currentEntityType, currentEntityId}, hitCurrentREWI>
        Map<String, Double> svIdcontextId_productValueMap = new HashMap<>();                                            //统计值Map<sentenceVectorId_contextId, 统计数据值>
        Integer maxValueSentenceVectorId = null;                                                                        //最高匹配值的sentenceVectorId（和下面的contextId绑定）
        Integer maxValueContextId = null;                                                                               //最高匹配值的ContextId
        Double maxValue = 0d;                                                                                           //最高匹配值（此值是匹配上的实体的(1d / currentSVSize * currentWeight) * 3 + (volumeIncrement * historyWeight)的和值）


        //2.计算最佳上下文：准备好关联数据，选好最高得分的contextId，确定换参匹配，匹配哪个历史上下文
        for (SentenceVector sentenceVector : sentenceVectors) {                                                         //句子端：查询本次检索出来的句子向量
            Integer sentenceVectorId = sentenceVector.getId();                                                          //句子向量ID
            List<List<REntityWordInfo>> currentREntityWordInfosList = sentenceVector.getrEntityWordInfosList();         //遍历当前句子向量里面的各个REWIs
            int currentSVSize = currentREntityWordInfosList.size();                                                     //句子向量长度

            for (List<REntityWordInfo> currentREntityWordInfos : currentREntityWordInfosList) {
                for (REntityWordInfo currentREntityWordInfo : currentREntityWordInfos) {
                    Integer currentEntityType = currentREntityWordInfo.getEntityType();
                    Integer currentEntityId = currentREntityWordInfo.getEntityId();
                    String currentEntityTypeId = currentREntityWordInfo.getEntityTypeId();                              //获取REWI的entityTypeId
                    Double currentWeight = currentREntityWordInfo.getWeightMap().get(sentenceVectorId);                 //获取本句子向量的此REWI的权重

                    List<REntityWordInfo> historyREntityWordInfos = historyREWIMap.get(currentEntityTypeId);            //历史端：尝试搜索此entityTypeId是否存在于历史匹配的REWI集合里面
                    if(historyREntityWordInfos != null && historyREntityWordInfos.size() > 0){                          //如果本次entityTypeId的REWI碰上了历史REWI集合
                        CoordinateKey coordinateKey = new CoordinateKey(sentenceVectorId, currentEntityType, currentEntityId);  //构建此唯一变量组作为key
                        //1.先将获得的能匹配上的 currentREntityWordInfo 对象放到关联Map中
                        hitCurrentREntityWordInfoMap.put(coordinateKey, currentREntityWordInfo);

                        for (REntityWordInfo historyREntityWordInfo : historyREntityWordInfos) {
                            //2.获取此historyREWI的相关数据，对其值累计到统计数据中
                            Integer contextId = historyREntityWordInfo.getContextId();                                  //这个小历史REWI集合虽然共有一个相同的entityTypeId，但有不同的contextId，即会话版本不同
                            Double volumeIncrement = historyVolumeIncrementMap.get(contextId);                          //获得此会话的数量单元增量
                            volumeIncrement = volumeIncrement != null ? volumeIncrement : 0d;                           //volumeIncrement确保有值
                            Map<Integer, Double> weightMap = historyREntityWordInfo.getWeightMap();
                            Double historyWeight = weightMap.get(sentenceVectorId);                                     //获得此实体在这个会话里面的权重
                            historyWeight = historyWeight != null ? historyWeight : 0d;                                 //排除historyWeight为null的情况

                            double productValueIncrement = (1d / currentSVSize * currentWeight) * 3 + (volumeIncrement * historyWeight);//当前句子REWI积值*3 和历史REWI积值 之和
                            System.out.println(currentContextId + " ---------- " + contextId + "   historyREWI ： " + historyREntityWordInfo.getEntityName() + " ----->  1d / " + currentSVSize + " * " + currentWeight + " * 3" + " + ( " + volumeIncrement + " * " + historyWeight + " )");
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
        }

        if(maxValueContextId != null){
            //3.换参，设置历史规则为当前匹配规则：设置changeParamOptimalSvRuleInfo，并修改里面替换的参数，包括words和matchedREntityWordInfos
            SVRuleInfo changeParamOptimalSvRuleInfo = contextId_svRuleInfoMap.get(maxValueContextId);                   //这里通过maxValueContextId获取的SVRuleInfo对象作为换参匹配返回的changeParamOptimalSvRuleInfo，有待后面对比全参匹配进行选择
            List<String> words = changeParamOptimalSvRuleInfo.getWords();
            List<REntityWordInfo> matchedREntityWordInfosModified = new ArrayList<>();                                  //准备一个新的REWI集合，作为换参SVRuleInfo的匹配REWI集合
            List<REntityWordInfo> matchedREntityWordInfos = changeParamOptimalSvRuleInfo.getMatchedREntityWordInfos();             //获取旧的REWI集合
            for(int i = 0; i < matchedREntityWordInfos.size(); i ++){
                REntityWordInfo matchedREntityWordInfo = matchedREntityWordInfos.get(i);
                Integer entityType = matchedREntityWordInfo.getEntityType();
                Integer entityId = matchedREntityWordInfo.getEntityId();
                Map<Integer, Double> historyWeightMap = matchedREntityWordInfo.getWeightMap();
                CoordinateKey coordinateKey = new CoordinateKey(maxValueSentenceVectorId, entityType, entityId);
                REntityWordInfo hitCurrentREntityWordInfo = hitCurrentREntityWordInfoMap.get(coordinateKey);
                if(hitCurrentREntityWordInfo != null){
                    hitCurrentREntityWordInfo.setContextId(currentContextId);
                    hitCurrentREntityWordInfo.setWeightMap(historyWeightMap);                                           //注意权重还是要用历史规则里面REWI里面的权重
                    matchedREntityWordInfosModified.add(hitCurrentREntityWordInfo);
                    words.set(i, hitCurrentREntityWordInfo.getWord());
                }else{
                    matchedREntityWordInfo.setContextId(currentContextId);
                    matchedREntityWordInfosModified.add(matchedREntityWordInfo);
                }
            }
            changeParamOptimalSvRuleInfo.setMatchedREntityWordInfos(matchedREntityWordInfosModified);                   //重新设置修改的REWI集合

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

            if(historyEntitiesCount <= currentEntitiesCount || currentCoreEntitiesCount > 2 || currentHitEntityWeightRate < 0.6d){  //满足这3个条件之一的，我心里就不踏实了，就强制它走一遍全参匹配
                changeParamOptimalSvRuleInfo.setEnsureFinal(false);
            }else{
                changeParamOptimalSvRuleInfo.setEnsureFinal(true);
            }
            return new ComponentBizResult("CPMC_S", Constant.STORE_LOCAL_REMOTE, changeParamOptimalSvRuleInfo);          //此结果在本地和远程都要存储
        }else{
            return new ComponentBizResult("CPMC_F");
        }

    }
}
