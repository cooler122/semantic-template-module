package com.cooler.semantic.service.internal;

import com.cooler.semantic.entity.Entity;

import java.util.List;

public interface EntityService extends BaseService<Entity>{

    /**
     * 根据entityIds查询Entity集合
     * @param entityIds
     * @return
     */
    List<Entity> selectByEIds(List<Integer> entityIds);
}
