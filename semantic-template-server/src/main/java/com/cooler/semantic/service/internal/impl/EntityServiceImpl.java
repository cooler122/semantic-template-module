package com.cooler.semantic.service.internal.impl;

import com.cooler.semantic.dao.EntityMapper;
import com.cooler.semantic.entity.Entity;
import com.cooler.semantic.service.internal.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("entityService")
public class EntityServiceImpl implements EntityService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return entityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Entity record) {
        return entityMapper.insert(record);
    }

    @Override
    public int insertSelective(Entity record) {
        return entityMapper.insertSelective(record);
    }

    @Override
    public Entity selectByPrimaryKey(Integer id) {
        return entityMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Entity record) {
        return entityMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Entity record) {
        return entityMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Entity> selectByEIds(List<Integer> entityIds) {
        return entityMapper.selectByEIds(entityIds);
    }
}
