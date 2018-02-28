package com.cooler.semantic.service;

import com.cooler.semantic.entity.LogDataProcess;

public interface LogDataProcessService {

    int deleteByPrimaryKey(Integer id);

    int insert(LogDataProcess record);

    int insertSelective(LogDataProcess record);

    LogDataProcess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogDataProcess record);

    int updateByPrimaryKey(LogDataProcess record);

}
