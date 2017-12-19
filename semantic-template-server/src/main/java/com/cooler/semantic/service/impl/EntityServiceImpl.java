package com.cooler.semantic.service.impl;

import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.model.SentenceInfo;
import com.cooler.semantic.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("entityService")
public class EntityServiceImpl implements EntityService {
    private Logger logger = LoggerFactory.getLogger(EntityServiceImpl.class);
    @Override
    public List<SentenceInfo> entitySearch(SemanticParserRequest request, List<SentenceInfo> sentenceInfos) {
        return null;
    }
}
