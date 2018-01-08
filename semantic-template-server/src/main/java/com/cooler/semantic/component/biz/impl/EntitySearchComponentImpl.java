package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.entity.REntityWord;
import com.cooler.semantic.entity.WordCN;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.internal.REntityWordService;
import com.cooler.semantic.service.internal.WordCNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("entitySearchComponent")
public class EntitySearchComponentImpl extends FunctionComponentBase<List<SentenceVector>, List<SentenceVector>> {

    private static Logger logger = LoggerFactory.getLogger(EntitySearchComponentImpl.class.getName());

    @Autowired
    private REntityWordService rEntityWordService;

    @Autowired
    private WordCNService wordCNService;

    public EntitySearchComponentImpl() {
        super("ESC", "SO_3", "sentenceVectors", "sentenceVectors");
    }

    @Override
    protected ComponentBizResult<List<SentenceVector>> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("SO_3.词语-实体检索");

        Integer accountId = contextOwner.getAccountId();

        Map<String, List<REntityWordInfo>> rEntityWordInfosMap = new HashMap<>();

        //1.先将所有分词组合出现的分词段收集起来，这里使用Set就直接去重了，查询出这些词语的REntityWord对象集合
        Set<String> allWords = new HashSet<>();
        for (SentenceVector sentenceVector : sentenceVectors){
            List<String> words = sentenceVector.getWords();
            allWords.addAll(words);
        }
        List<REntityWord> rEntityWords = rEntityWordService.selectByAIdWords(accountId, allWords);              //有一些词语可能查不出来，但要考虑处理这种情况
        System.out.println(JSON.toJSONString(rEntityWords));

        //2.将这些词语设置到Map中，为每一个词语准备一个List
        for (String word : allWords) {
            rEntityWordInfosMap.put(word, new ArrayList<REntityWordInfo>());                                    //为每一个分词段设置一个List<RWEI>
        }

        //3.将所有能归属到字符串实体的词语放置到Map中各个词语名下，作为其值
        for (REntityWord rEntityWord : rEntityWords) {
            String word = rEntityWord.getWord();
            List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosMap.get(word);

            REntityWordInfo rEntityWordInfo = new REntityWordInfo();
            rEntityWordInfo.setWordId(rEntityWord.getWordId());
            rEntityWordInfo.setWord(rEntityWord.getWord());
            rEntityWordInfo.setEntityId(rEntityWord.getEntityId());
            rEntityWordInfo.setEntityName(rEntityWord.getEntityName());
            rEntityWordInfo.setNormalWord(rEntityWord.getNormalWord());
            rEntityWordInfo.setEntityType(1);                                                   //1表示字符串实体
            rEntityWordInfo.setEntityTypeId("1_" + rEntityWord.getEntityId());

            rEntityWordInfos.add(rEntityWordInfo);

            allWords.remove(word);                                                              //删除掉查询出了实体的词语，剩下的词语查不到实体了
        }

        if(allWords.size() > 0){
            //4.剩下的实体进行其他方式进行归属
            //TODO:allWords现在剩下的词语是查询不到实体的词语，但也许这些词语归属于其他类型的实体，这里需要进行其他类型的实体归属过程，有待后续实现

            //5.最终剩下的，只能作为常量实体存在于模板之中，实体类型为0。
            // 这里分两种情况：a.这个词在词表中有，但在关系表中没有指定它是哪种实体；b.这个词在词表中也没有，你没办法判断其是否有意义，所以还是需要添加进去（宁滥勿缺）
            List<WordCN> wordCNs = wordCNService.selectByWords(accountId, allWords);

            for (WordCN wordCN : wordCNs) {
                Integer wordId = wordCN.getId();
                String word = wordCN.getWord();

                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosMap.get(word);
                if(rEntityWordInfos == null) {
                    rEntityWordInfos = new ArrayList<>();
                }
                REntityWordInfo rEntityWordInfo = new REntityWordInfo();
                rEntityWordInfo.setWordId(wordId);
                rEntityWordInfo.setWord(word);
                rEntityWordInfo.setEntityId(wordId);                                                                    //这里是常量实体，则将entityId和entityName设置为wordID和word
                rEntityWordInfo.setEntityName(word);
                rEntityWordInfo.setNormalWord(word);
                rEntityWordInfo.setEntityType(0);
                rEntityWordInfo.setEntityTypeId("0_" + wordId);
                rEntityWordInfos.add(rEntityWordInfo);

                rEntityWordInfosMap.put(word, rEntityWordInfos);
            }
        }

        //6.将归属好的实体Map，一一取出来，放置到SentenceVector集合的REntityWordInfos里面
        for (SentenceVector sentenceVector : sentenceVectors) {
            List<List<REntityWordInfo>> rEntityWordInfosList = new ArrayList<>();
            List<String> words = sentenceVector.getWords();
            List<Double> weights = sentenceVector.getWeights();
            List<String> natures = sentenceVector.getNatures();
            for(int i = 0; i < words.size(); i ++){
                String word = words.get(i);
                List<REntityWordInfo> rEntityWordInfos = rEntityWordInfosMap.get(word);
                for (REntityWordInfo rEntityWordInfo : rEntityWordInfos) {
                    rEntityWordInfo.setWeight(weights.get(i));                                                          //将weight和nature复制到rEntityWordInfo中
                    rEntityWordInfo.setNature(natures.get(i));
                }
                rEntityWordInfosList.add(rEntityWordInfos);
            }
            sentenceVector.setrEntityWordInfosList(rEntityWordInfosList);
        }
        return new ComponentBizResult("ESC_S", Constant.STORE_LOCAL, sentenceVectors);
    }


}
