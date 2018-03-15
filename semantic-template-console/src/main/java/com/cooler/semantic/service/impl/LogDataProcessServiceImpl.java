package com.cooler.semantic.service.impl;

import com.cooler.semantic.dao.LogDataProcessMapper;
import com.cooler.semantic.dto.LogDataProcessDTO;
import com.cooler.semantic.entity.LogDataProcess;
import com.cooler.semantic.service.LogDataProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("logDataProcessService")
public class LogDataProcessServiceImpl implements LogDataProcessService {

    @Autowired
    private LogDataProcessMapper logDataProcessMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return logDataProcessMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(LogDataProcess record) {
        return logDataProcessMapper.insert(record);
    }

    @Override
    public int insertSelective(LogDataProcess record) {
        return logDataProcessMapper.insertSelective(record);
    }

    @Override
    public LogDataProcess selectByPrimaryKey(Integer id) {
        return logDataProcessMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(LogDataProcess record) {
        return logDataProcessMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(LogDataProcess record) {
        return logDataProcessMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<LogDataProcessDTO> selectByAIdUIdDateTime(Integer accountId, Integer userId, String fromDateTime, String toDateTime) {
        return logDataProcessMapper.selectByAIdUIdDateTime(accountId, userId, fromDateTime, toDateTime);
    }


}
