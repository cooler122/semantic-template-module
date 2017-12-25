package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.EntitySearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("entityService")
public class EntitySearchServiceImpl implements EntitySearchService {
    private Logger logger = LoggerFactory.getLogger(EntitySearchServiceImpl.class);
    @Override
    public List<SentenceVector> entitySearch(SemanticParserRequest request, List<SentenceVector> sentenceVectors) {
        return null;
    }
}
