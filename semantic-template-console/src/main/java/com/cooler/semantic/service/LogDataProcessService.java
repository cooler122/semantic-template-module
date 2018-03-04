package com.cooler.semantic.service;

import com.cooler.semantic.entity.LogDataProcess;

import java.util.List;

public interface LogDataProcessService {

    int deleteByPrimaryKey(Integer id);

    int insert(LogDataProcess record);

    int insertSelective(LogDataProcess record);

    LogDataProcess selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogDataProcess record);

    int updateByPrimaryKey(LogDataProcess record);

    /**
     * 按账号、用户以及起始和终止日期、小时、分钟查询
     * @param accountId
     * @param userId
     * @param fromDateTime
     * @param toDateTime
     * @return
     */
    List<LogDataProcess> selectByAIdUIdDateTime(Integer accountId,
                                                Integer userId,
                                                String fromDateTime,
                                                String toDateTime );

}
