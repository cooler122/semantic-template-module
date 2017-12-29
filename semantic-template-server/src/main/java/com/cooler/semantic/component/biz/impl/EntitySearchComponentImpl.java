package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.entity.WordCN;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.internal.EntityService;
import com.cooler.semantic.service.internal.WordCNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("entitySearchComponent")
public class EntitySearchComponentImpl extends FunctionComponentBase<List<SentenceVector>, List<SentenceVector>> {

    private static Logger logger = LoggerFactory.getLogger(EntitySearchComponentImpl.class.getName());

    @Autowired
    private EntityService entityService;
    @Autowired
    private WordCNService wordCNService;

    public EntitySearchComponentImpl() {
        super("ESC", "SO_3", "sentenceVectors", "sentenceVectors");
    }

    @Override
    protected ComponentBizResult<List<SentenceVector>> runBiz(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        logger.info("SO_3.词语-实体检索");

        System.out.println(JSON.toJSONString(sentenceVectors));
        Set<String> allWords = new HashSet<>();
        for (SentenceVector sentenceVector : sentenceVectors) {
            List<String> words = sentenceVector.getWords();
            allWords.addAll(words);
        }
        List<WordCN> wordCNs = wordCNService.selectByWords(allWords);




        return new ComponentBizResult("ESC_S", 1, sentenceVectors);
    }


}
