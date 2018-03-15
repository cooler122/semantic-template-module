package com.cooler.semantic.service.impl;

import com.cooler.semantic.dao.LogDataCalculationMapper;
import com.cooler.semantic.entity.LogDataCalculation;
import com.cooler.semantic.service.LogDataCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("logDataCalulationService")
public class LogDataCalculationServiceImpl implements LogDataCalculationService {

    @Autowired
    private LogDataCalculationMapper logDataCalculationMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return logDataCalculationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(LogDataCalculation record) {
        return logDataCalculationMapper.insert(record);
    }

    @Override
    public int insertSelective(LogDataCalculation record) {
        return logDataCalculationMapper.insertSelective(record);
    }

    @Override
    public LogDataCalculation selectByPrimaryKey(Integer id) {
        return logDataCalculationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(LogDataCalculation record) {
        return logDataCalculationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(LogDataCalculation record) {
        return logDataCalculationMapper.updateByPrimaryKey(record);
    }

    @Override
    public LogDataCalculation selectBy4Params(Integer accountId, Integer userId, Integer contextId, Long currentTimeMillis) {
        return logDataCalculationMapper.selectBy4Params(accountId, userId, contextId, currentTimeMillis);
    }

    @Override
    public int checkBy4Params(Integer accountId, Integer userId, Integer contextId, Long currentTimeMillis) {
        return logDataCalculationMapper.checkBy4Params(accountId, userId, contextId, currentTimeMillis);
    }

    @Override
    public LogDataCalculation getLogDataCalculation(Integer accountId, Integer userId, Integer contextId, Long currentTimeMillis) {
        return logDataCalculationMapper.getLogDataCalculation(accountId, userId, contextId, currentTimeMillis);
    }
}
