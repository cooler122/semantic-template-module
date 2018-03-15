package com.cooler.semantic.dao;

import com.cooler.semantic.entity.RAnaphoraEntity;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface RAnaphoraEntityMapper extends BaseMapper<RAnaphoraEntity> {
    /**
     * 通过指代实体的ID集合，查询指代实体关系集合
     * @param accountId
     * @param hitAnaphoraEntityIds
     * @return
     */
    List<RAnaphoraEntity> selectByEntityIds(@Param("accountId") Integer accountId, @Param("anaphoraEntityIds") List<Integer> hitAnaphoraEntityIds);
}