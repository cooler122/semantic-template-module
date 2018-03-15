package com.cooler.semantic.service;

import com.cooler.semantic.entity.LogDataCalculation;

public interface LogDataCalculationService {
    int deleteByPrimaryKey(Integer id);

    int insert(LogDataCalculation record);

    int insertSelective(LogDataCalculation record);

    LogDataCalculation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogDataCalculation record);

    int updateByPrimaryKey(LogDataCalculation record);

    LogDataCalculation selectBy4Params(Integer accountId, Integer userId, Integer contextId, Long currentTimeMillis);

    int checkBy4Params(Integer accountId, Integer userId, Integer contextId, Long currentTimeMillis);

    LogDataCalculation getLogDataCalculation(Integer accountId, Integer userId, Integer contextId, Long currentTimeMillis);
}
