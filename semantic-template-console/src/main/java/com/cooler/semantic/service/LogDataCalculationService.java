package com.cooler.semantic.service;

import com.cooler.semantic.entity.LogDataCalculation;

public interface LogDataCalculationService {
    int deleteByPrimaryKey(Integer id);

    int insert(LogDataCalculation record);

    int insertSelective(LogDataCalculation record);

    LogDataCalculation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogDataCalculation record);

    int updateByPrimaryKey(LogDataCalculation record);

}
