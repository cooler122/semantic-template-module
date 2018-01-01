package com.cooler.semantic.dao;

import com.cooler.semantic.entity.Entity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EntityMapper extends BaseMapper<Entity>{

    /**
     * 根据entityIds查询Entity集合
     * @param entityIds
     * @return
     */
    List<Entity> selectByEIds(@Param("entityIds") List<Integer> entityIds);
}