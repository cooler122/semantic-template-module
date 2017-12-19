package com.cooler.semantic.service.impl;

import com.cooler.semantic.model.SentenceInfo;
import com.cooler.semantic.service.WordSegregateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("wordSegregateService")
public class WordSegregateServiceImpl implements WordSegregateService {
    private Logger logger = LoggerFactory.getLogger(WordSegregateServiceImpl.class);

    @Override
    public List<SentenceInfo> wordSegregate(String sentence, Integer accountId, List<Integer> domainIds, List<Integer> selectorIds, boolean isDropPunctuation) {
        logger.info("分词...");
        return null;
    }
}
