package com.cooler.semantic.dao;

public interface BaseMapper<O> {
    int deleteByPrimaryKey(Integer id);

    int insert(O record);

    int insertSelective(O record);

    O selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(O record);

    int updateByPrimaryKey(O record);
}
