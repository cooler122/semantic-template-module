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

    /**
     * 根据accountId和ruleId来查询相关RRE集合
     * @param accountId
     * @param enableReferRuleId
     * @return
     */
    List<RRuleEntity> selectNecessaryByAIdRId(@Param("accountId") Integer accountId, @Param("ruleId") Integer enableReferRuleId);

    /**
     * 根据所有ruleIds查询RRE集合(必须实体）
     * @param accountId
     * @param ruleIds
     * @return
     */
    List<RRuleEntity> selectByRuleIds(@Param("accountId") Integer accountId, @Param("ruleIds") List<Integer> ruleIds);
}