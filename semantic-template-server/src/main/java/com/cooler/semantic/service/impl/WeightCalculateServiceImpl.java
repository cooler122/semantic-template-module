package com.cooler.semantic.service.impl;

import com.cooler.semantic.model.SentenceVector;
import com.cooler.semantic.service.WeightCalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("weightCalculateService")
public class WeightCalculateServiceImpl implements WeightCalculateService {

    private Logger logger = LoggerFactory.getLogger(WeightCalculateServiceImpl.class);

    @Override
    public void calculateWeight(List<SentenceVector> sentenceVectors) {
    }
}
