package com.cooler.semantic.service.external.impl;

import com.cooler.semantic.constant.Constant;
import com.cooler.semantic.dao.AnaphoraEntityMapper;
import com.cooler.semantic.dao.AnaphoraWordMapper;
import com.cooler.semantic.model.ContextOwner;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.external.AnaphoraResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("anaphoraResolutionService")
public class AnaphoraResolutionServiceImpl implements AnaphoraResolutionService {
    @Autowired
    private AnaphoraEntityMapper anaphoraEntityMapper;
    @Autowired
    private AnaphoraWordMapper anaphoraWordMapper;

    @Override
    public List<SentenceVector> anaphoraResolution(ContextOwner contextOwner, List<SentenceVector> sentenceVectors) {
        List<String> words = new ArrayList<>();


    }
}
