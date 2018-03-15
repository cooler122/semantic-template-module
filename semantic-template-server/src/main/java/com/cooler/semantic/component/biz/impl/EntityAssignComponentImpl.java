package com.cooler.semantic.component.biz.impl;

import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.REntityWord;
import com.cooler.semantic.entity.WordCN;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.AnaphoraResolutionService;
import com.cooler.semantic.service.internal.REntityWordService;
import com.cooler.semantic.service.internal.WordCNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("entityAssignComponent")
public class EntityAssignComponentImpl extends FunctionComponentBase<List<SentenceVector>, List<SentenceVector>> {

    private static Logger logger = LoggerFactory.getLogger(EntityAssignComponentImpl.class.getName());

    @Autowired
    private REntityWordService rEntityWordService;
    @Autowired
    private WordCNService wordCNService;
    @Autowired
    private AnaphoraResolutionService anaphoraResolutionService;

    public EntityAssignComponentImpl() {
        super("EAC", "sentenceVectors", "sentenceVectors");
    }

    @Override
    protected ComponentBizResult<List<SentenceVector>> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.trace("EAC.实体归属");
        Integer accountId = contextOwner.getCoreAccountId();
        Integer contextId = contextOwner.getContextId();
        boolean hadAnaphoraResolution = false;
        Map<String, List<REntityWordInfo>> rEntityWordInfosMap = new HashMap<>();
        int sentenceVectorSize = sentenceVectors.size();

        //1.先将所有分词组合出现的分词段收集起来，这里使用Set就直接去重了，查询出这些词语的REntityWord对象集合
        Set<String> allWords = new HashSet<>();
        for (SentenceVector sentenceVector : sentenceVectors){
            List<String> words = sentenceVector.getWords();
            allWords.addAll(words);
        }

        //2.将这些词语设置到Map中，为每一个词语准备一个List
        for (String word : allWords) {
            rEntityWordInfosMap.put(word, new ArrayList<REntityWordInfo>());                                            //为每一个分词段设置一个List<RWEI>
        }

        //3.1.指代性实体归属：查找指代性词语，归属指代性实体
        Map<String, List<REntityWordInfo>> word_anaphoraEntitiesMap = anaphoraResolutionService.anaphoraResolution(contextOwner, allWords, sentenceVectorSize);
        if(word_anaphoraEntitiesMap != null && word_anaphoraEntitiesMap.size() > 0){
            Set<String> anaphoraWords = word_anaphoraEntitiesMap.keySet();                                              //已经归属好的指代性词语（实际上来源于上一轮的REW）
            for (String anaphoraWord : anaphoraWords) {
                List<REntityWordInfo> rEntityWordInfos = word_anaphoraEntitiesMap.get(anaphoraWord);
                String wordModified = rEntityWordInfos.get(0).getWord();                                                //第一，此rEntityWordInfos必然有元素；第二，里面所有元素的word相同
                rEntityWordInfosMap.put(wordModified, rEntityWordInfos);
            }
            for (SentenceVector sentenceVector : sentenceVectors){                                                      //修改原句中的word词组，改变它的指代词语
                List<String> words = sentenceVector.getWords();
                for (int i = 0; i < words.size(); i ++) {
                    String sentenceWord = words.get(i);
                    if(anaphoraWords.contains(sentenceWord)){
                        List<REntityWordInfo> rEntityWordInfos = word_anaphoraEntitiesMap.get(sentenceWord);
                        String wordModified = rEntityWordInfos.get(0).getWord();
                        words.set(i, wordModified);
                    }
                }
            }
            hadAnaphoraResolution = true;                                                                              //标记已经经过指代消解了
            allWords.removeAll(anaphoraWords);                                                                          //删除已经归属好的词语
        }

        //3.2.字符串实体归属：将所有能归属到字符串实体的词语放置到Map中各个词语名下，作为其值
        List<REntityWord> rEntityWords = rEntityWordService.selectByAIdWords(accountId, allWords);                      //指代词语归属后，一定剩下一些词语，进行字符串实体归属
        for (REntityWord rEntityWord : rEntityWords) {
            String word = rEntityWord.getWord();
            List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosMap.get(word);

            REntityWordInfo rEntityWordInfo = new REntityWordInfo(sentenceVectorSize);
            rEntityWordInfo.setWordId(rEntityWord.getWordId());
            rEntityWordInfo.setWord(rEntityWord.getWord());
            rEntityWordInfo.setEntityId(rEntityWord.getEntityId());
            rEntityWordInfo.setEntityName(rEntityWord.getEntityName());
            rEntityWordInfo.setNormalWord(rEntityWord.getNormalWord());
            rEntityWordInfo.setEntityType(Constant.STRINGS_ENTIRY);                                                     //表示字符串实体，实际值为1
            rEntityWordInfo.setEntityTypeId(Constant.STRINGS_ENTIRY + "_" + rEntityWord.getEntityId());
            rEntityWordInfo.setContextId(contextId);                                                                    //设置上下文版本号

            rEntityWordInfos.add(rEntityWordInfo);

            allWords.remove(word);                                                                                      //删除掉查询出了实体的词语，剩下的词语查不到实体了
        }

        if(allWords.size() > 0){
            //3.3.剩下的实体进行其他方式进行归属
            //TODO:allWords现在剩下的词语是查询不到实体的词语，但也许这些词语归属于其他类型的实体，这里需要进行其他类型的实体归属过程，有待后续实现(例如有时间实体、日期实体、数值实体等）

            //3.4.词语实体归属：最终剩下的，只能作为常量实体存在于模板之中，实体类型为0。
            // 这里分两种情况：a.这个词在词表中有，但在关系表中没有指定它是哪种实体；b.这个词在词表中也没有，你没办法判断其是否有意义，所以还是需要添加进去（宁滥勿缺）
            List<WordCN> wordCNs = wordCNService.selectByWords(accountId, allWords);

            for (WordCN wordCN : wordCNs) {
                Integer wordId = wordCN.getId();
                String word = wordCN.getWord();

                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosMap.get(word);                                 //此集合已经在上面设置过集合对象，不可能为null
                REntityWordInfo rEntityWordInfo = new REntityWordInfo(sentenceVectorSize);
                rEntityWordInfo.setWordId(wordId);
                rEntityWordInfo.setWord(word);
                rEntityWordInfo.setEntityId(wordId);                                                                    //这里是常量实体，则将entityId和entityName设置为wordID和word
                rEntityWordInfo.setEntityName(word);
                rEntityWordInfo.setNormalWord(word);
                rEntityWordInfo.setEntityType(Constant.WORD_ENTITY);                                                    //标识词语实体，实际值为0
                rEntityWordInfo.setEntityTypeId(Constant.WORD_ENTITY + "_" + wordId);
                rEntityWordInfo.setContextId(contextId);

                rEntityWordInfos.add(rEntityWordInfo);

                rEntityWordInfosMap.put(word, rEntityWordInfos);
            }
        }

        //4.将归属好的实体Map，一一取出来，放置到SentenceVector集合的REntityWordInfos里面
        for (SentenceVector sentenceVector : sentenceVectors) {
            Integer sentenceVectorId = sentenceVector.getId();
            List<List<REntityWordInfo>> rEntityWordInfosList = new ArrayList<>();
            List<String> words = sentenceVector.getWords();
            List<Double> weights = sentenceVector.getWeights();
            List<String> natures = sentenceVector.getNatures();
            for(int i = 0; i < words.size(); i ++){
                String word = words.get(i);
                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosMap.get(word);
                for (int j = 0; j < rEntityWordInfos.size(); j ++) {
                    REntityWordInfo rEntityWordInfo = rEntityWordInfos.get(j);

                    List<Double> weightsTmp = rEntityWordInfo.getWeights();

                    weightsTmp.set(sentenceVectorId, weights.get(i));                                                    //将weight和nature复制到rEntityWordInfo中
                    rEntityWordInfo.setNature(natures.get(i));
                }
                rEntityWordInfosList.add(rEntityWordInfos);
            }
            sentenceVector.setrEntityWordInfosList(rEntityWordInfosList);
        }

        if(!hadAnaphoraResolution){                                                                                     //没有经过指代消解
            return new ComponentBizResult("EAC_S_N", Constant.STORE_LOCAL, sentenceVectors);
        }else{
            return new ComponentBizResult("EAC_S_Y", Constant.STORE_LOCAL, sentenceVectors);
        }

    }
}
