package com.cooler.semantic.dao;

import com.cooler.semantic.entity.RRuleEntity;
import com.cooler.semantic.model.REntityWordInfo;
import com.cooler.semantic.model.SVRuleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RRuleEntityMapper extends BaseMapper<RRuleEntity> {

    /**
     * 根据个人账户accountId和REntityWordInfo相关参数，获取RRuleEntity关系集合
     * @param accountId
     * @param rEntityWordInfosParam
     * @return
     */
    List<RRuleEntity> selectByREntityWordInfos(@Param("accountId") Integer accountId, @Param("rEntityWordInfos") List<REntityWordInfo> rEntityWordInfosParam);

    /**
     * 根据SVRuleInfo集合来查询相关的RRuleEntity集合
     * @param accountId
     * @param svRuleInfos
     * @return
     */
    List<RRuleEntity> selectBySVRuleInfos(@Param("accountId") Integer accountId, @Param("svRuleInfos") List<SVRuleInfo> svRuleInfos);
}