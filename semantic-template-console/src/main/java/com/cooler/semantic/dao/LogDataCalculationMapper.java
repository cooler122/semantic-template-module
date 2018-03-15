package com.cooler.semantic.dao;

import com.cooler.semantic.entity.LogDataCalculation;
import org.apache.ibatis.annotations.Param;

public interface LogDataCalculationMapper extends BaseMapper<LogDataCalculation> {

    /**
     * 根据4个值来确定一个LogDataCalculation对象
     * @param accountId
     * @param userId
     * @param contextId
     * @param currentTimeMillis
     * @return
     */
    LogDataCalculation selectBy4Params(@Param("accountId") Integer accountId, @Param("userId") Integer userId, @Param("contextId") Integer contextId, @Param("currentTimeMillis") Long currentTimeMillis);

    /**
     * 根据4个值来判断DB中是否有数据
     * @param accountId
     * @param userId
     * @param contextId
     * @param currentTimeMillis
     * @return
     */
    int checkBy4Params(@Param("accountId") Integer accountId, @Param("userId") Integer userId, @Param("contextId") Integer contextId, @Param("currentTimeMillis") Long currentTimeMillis);

    /**
     * 获取LogDataCalculation一个匹配模式下的数据
     * @param accountId
     * @param userId
     * @param contextId
     * @param currentTimeMillis
     * @return
     */
    LogDataCalculation getLogDataCalculation(@Param("accountId") Integer accountId, @Param("userId") Integer userId,  @Param("contextId") Integer contextId,  @Param("currentTimeMillis") Long currentTimeMillis);
}