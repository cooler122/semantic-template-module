package com.cooler.semantic.dao;

import com.cooler.semantic.entity.AccountConfiguration;
import org.apache.ibatis.annotations.Param;

public interface AccountConfigurationMapper extends BaseMapper<AccountConfiguration> {
    /**
     * 按照accountId和userId来查询账户配置
     * @param coreAccountId 账户ID
     * @param userId    用户编号
     * @return  配置信息
     */
    AccountConfiguration selectAIdUId(@Param("accountId")Integer coreAccountId, @Param("userId")Integer userId);
}