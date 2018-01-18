package com.cooler.semantic.component.biz.impl;

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
        super("CPMC", "SO-6 ~ SO-7", "sentenceVectors", "optimalSvRuleInfo");
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

        Map<CoordinateKey, REntityWordInfo> hitCurrentREntityWordInfoMap = new HashMap<>();                             //关联数据Map<{sentenceVectorId, currentEntityType, currentEntityId}, hitCurrentREWI>
        Map<String, Double> svIdcontextId_productValueMap = new HashMap<>();                                            //统计值Map<sentenceVectorId_contextId, 统计数据值>
        Integer maxValueSentenceVectorId = null;                                                                        //最高匹配值的sentenceVectorId（和下面的contextId绑定）
        Integer maxValueContextId = null;                                                                               //最高匹配值的ContextId
        Double maxValue = 0d;                                                                                           //最高匹配值

        //2.准备好关联数据，选好最高得分的contextId，确定换参匹配，匹配哪个历史上下文
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
                            Map<Integer, Double> weightMap = historyREntityWordInfo.getWeightMap();
                            Double historyWeight = weightMap.get(sentenceVectorId);                                     //获得此实体在这个会话里面的权重

                            double productValueIncrement = 1d / currentSVSize * currentWeight * volumeIncrement * historyWeight;//当前句子REWI和历史REWI的积值增量
                            Double productValue = svIdcontextId_productValueMap.get(sentenceVectorId + "_" + contextId);
                            productValue = (productValue != null ? productValue : 0d) + productValueIncrement;          //将上面的统计数据体放到统计数据Map中
                            svIdcontextId_productValueMap.put(sentenceVectorId + "_" + contextId, productValue);

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

        //3.替换最佳optimalSvRuleInfo，并修改里面替换的参数，包括words和matchedREntityWordInfos
        if(maxValueContextId != null){
            SVRuleInfo optimalSvRuleInfo = contextId_svRuleInfoMap.get(maxValueContextId);                              //这里通过maxValueContextId获取的SVRuleInfo对象就作为最终返回的optimalSvRuleInfo
            List<String> words = optimalSvRuleInfo.getWords();
            List<REntityWordInfo> matchedREntityWordInfosModified = new ArrayList<>();                                  //准备一个新的REWI集合，作为换参SVRuleInfo的匹配REWI集合
            List<REntityWordInfo> matchedREntityWordInfos = optimalSvRuleInfo.getMatchedREntityWordInfos();             //获取旧的REWI集合
            for(int i = 0; i < matchedREntityWordInfos.size(); i ++){
                REntityWordInfo matchedREntityWordInfo = matchedREntityWordInfos.get(i);
                Integer entityType = matchedREntityWordInfo.getEntityType();
                Integer entityId = matchedREntityWordInfo.getEntityId();
                CoordinateKey coordinateKey = new CoordinateKey(maxValueSentenceVectorId, entityType, entityId);
                REntityWordInfo hitCurrentREntityWordInfos = hitCurrentREntityWordInfoMap.get(coordinateKey);
                if(hitCurrentREntityWordInfos != null){
                    hitCurrentREntityWordInfos.setContextId(currentContextId);
                    matchedREntityWordInfosModified.add(hitCurrentREntityWordInfos);
                    words.set(i, hitCurrentREntityWordInfos.getWord());
                }else{
                    matchedREntityWordInfo.setContextId(currentContextId);
                    matchedREntityWordInfosModified.add(matchedREntityWordInfo);
                }
            }
            optimalSvRuleInfo.setMatchedREntityWordInfos(matchedREntityWordInfosModified);                              //重新设置修改的REWI集合

            return new ComponentBizResult("CPMC_S", Constant.STORE_LOCAL_REMOTE, optimalSvRuleInfo);          //此结果在本地和远程都要存储
        }else{
            return new ComponentBizResult("CPMC_F");
        }

    }
}
