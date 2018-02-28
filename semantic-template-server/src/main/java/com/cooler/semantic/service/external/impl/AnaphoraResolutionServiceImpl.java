package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.component.data.DataComponentBase;
import com.cooler.semantic.dao.AnaphoraWordMapper;
import com.cooler.semantic.dao.RAnaphoraEntityMapper;
import com.cooler.semantic.entity.AnaphoraWord;
import com.cooler.semantic.entity.RAnaphoraEntity;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import com.cooler.semantic.service.external.AnaphoraResolutionService;
import com.cooler.semantic.service.external.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("anaphoraResolutionService")
public class AnaphoraResolutionServiceImpl implements AnaphoraResolutionService {
    @Autowired
    private AnaphoraWordMapper anaphoraWordMapper;
    @Autowired
    private RAnaphoraEntityMapper rAnaphoraEntityMapper;
    @Autowired
    private RedisService<SVRuleInfo> redisService;

    @Override
    public Map<String, List<REntityWordInfo>> anaphoraResolution(ContextOwner contextOwner, Set<String> words) {
        //1.获取账户信息
        Integer accountId = contextOwner.getCoreAccountId();
        String last1OwnerIndex = contextOwner.getLast1OwnerIndex();

        //2.获取上轮对话数据
        DataComponentBase<SVRuleInfo> dataComponentBase = redisService.getCacheObject(last1OwnerIndex + "_optimalSvRuleInfo");                                           //获取上一轮对话数据
        if(dataComponentBase != null && dataComponentBase.getData() != null){                                         //上轮对话数据没有，直接返回空
            SVRuleInfo svRuleInfo = dataComponentBase.getData();
            Map<Integer, String> anaphoraEntityId_WordMap = new HashMap<>();                                            //指代词语对应的指代实体ID
            List<Integer> hitAnaphoraEntityIds = new ArrayList<>();                                                     //收集被用上的指代性实体ID

            //3.查询指代性词语对象，并将其各个相关字段收集起来
            List<AnaphoraWord> anaphoraWords = anaphoraWordMapper.selectByWords(words);                                 //传入各个词语，看看是否能搜索出指代词语
            for (AnaphoraWord anaphoraWord : anaphoraWords) {
                String apaphoraWord = anaphoraWord.getAnaphoraWord();                                                   //能命中的指代词语
                Integer anaphoraEntityId = anaphoraWord.getAnaphoraEntityId();                                          //指代词语隶属的指代实体ID
                hitAnaphoraEntityIds.add(anaphoraEntityId);                                                             //收集指代实体ID
                anaphoraEntityId_WordMap.put(anaphoraEntityId, apaphoraWord);                                           //将指代词语--指代实体ID，放入Map中
            }

            if(hitAnaphoraEntityIds.size() > 0){
                //4.根据指代实体ID集合和账户，查询用户指定的指代实体关联集合
                List<RAnaphoraEntity> anaphoraEntities = rAnaphoraEntityMapper.selectByEntityIds(accountId, hitAnaphoraEntityIds);  //通过指代实体ID集和用户账号，查询此账号下设定的指代实体关联集合

                //5.获取上轮对话的匹配REW集合，并转为Map形式
                List<REntityWordInfo> matchedREntityWordInfos = svRuleInfo.getMatchedREntityWordInfos();                //上一轮对话，匹配上的REW集合
                Map<Integer, REntityWordInfo> lastREntityWordInfoMap = new HashMap<>();                                 //将上衣轮所有REW转为Map形式，以备后面查被指代实体
                for (REntityWordInfo matchedREntityWordInfo : matchedREntityWordInfos) {
                    lastREntityWordInfoMap.put(matchedREntityWordInfo.getEntityId(), matchedREntityWordInfo);
                }

                //6.展开指代实体关联，查找能够替换这个指代实体的并且是上轮对话中的REW的实体是否存在
                Map<String, List<REntityWordInfo>> word_REntityWordInfoMap = new HashMap<>();                           //构建用来返回的Map
                for (RAnaphoraEntity anaphoraEntity : anaphoraEntities) {
                    Integer entityId = anaphoraEntity.getEntityId();                                                    //指代实体的entityId
//                    String entityName = anaphoraEntity.getEntityName();                                               //指代实体的名称
                    Integer referredEntityId = anaphoraEntity.getReferredEntityId();                                    //指代实体的替换实体Id
//                    String referredEntityName = anaphoraEntity.getReferredEntityName();                               //指代实体的替换实体名称
//                    Byte hasConstraint = anaphoraEntity.getHasConstraint();                                           //指代实体是否有约束 //TODO:这种约束有待后面再建立一张表来实现
                    String anaphoraWord = anaphoraEntityId_WordMap.get(entityId);                                       //获取此实体被替代的指代词语

                    REntityWordInfo lastREntityWordInfo = lastREntityWordInfoMap.get(referredEntityId);                 //上轮对话查询到了可以代替指代实体的REW了

                    if(lastREntityWordInfo != null){                                                                    //这个REW部位空
                        List<REntityWordInfo> rEntityWordInfos = word_REntityWordInfoMap.get(anaphoraWord);
                        if(rEntityWordInfos == null){                                                                   //为每一个指代词语设置隐射的指代实体REW集合
                            rEntityWordInfos = new ArrayList<>();
                            rEntityWordInfos.add(lastREntityWordInfo);
                        }
                        word_REntityWordInfoMap.put(anaphoraWord, rEntityWordInfos);                                    //在Map中设置每个指代词语的可替代REW集合
                    }
                }
                return word_REntityWordInfoMap;                                                                         //这个Map返回到外边
            }
        }
        return null;
    }
}
