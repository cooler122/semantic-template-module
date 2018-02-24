package com.cooler.semantic.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class SemanticParserRequest implements Serializable {

    //--------------------------------------------------------------------0.传入的句子
    /**
     * 请求信息（必须）
     */
    private String cmd;

    //--------------------------------------------------------------------1.账户参数AP
    /**
     * accountIds（账号列表，为了扩展查询匹配范围，可以传入多个账号，但是第一个一定要放核心账户ID，即coreAccountId）
     */
    private List<Integer> accountIds;

    private String password;
    /**
     * 核心账号的用户ID
     */
    private Integer userId;

    //--------------------------------------------------------------------2.配置参数CP
    /**
     * 段对话能被记忆的轮数
     */
    private int shortConversationMemorizedCount = 3;

    /**
     * 长对话能被记忆的轮数
     */
    private int longConversationMemorizedCount = 5;

    /**
     * 是否能跳出上下文
     */
    private boolean canBreakContext = true;

    /**
     * 能否缺失实体批量访问(默认一个个询问)
     */
    private boolean canBatchQuery = false;

    /**
     * 规则询问最大次数（如果缺失实体批量询问，则用此量）（要不要看后面效果!!!!!!!!!!!!!!!!!!!!）
     */
    private int ruleMaxQueryCount = 20;

    /**
     * 实体询问最大次数（如果缺失实体一个个询问，则用此量）
     */
    private int entityMaxQueryCount = 3;

    /**
     * 上下文两次请求的间隔时间（默认一分钟）
     */
    private int contextWaitTime = 60000;

    /**
     * 准确度阈值，设置后，低于这个准确度的rule不会被选中。（默认0.7）
     */
    private double accuracyThreshold  = 0.7;

    /**
     * 过程日志类型
     */
    private int processLogType = 0;

    /**
     * 计算日志类型
     */
    private int calculationLogType = 0;

    /**
     * 算法类型
     */
    private int algorithmType = 1;

    //--------------------------------------------------------------------3.上下文数据CD，以下是上次匹配产生的上下文参数（如果丢失，则相当于系统“失忆”，此方案是将上下文数据放在服务端的一种实施方案）
    /**
     * 上下文编号（用来支持上下文的字段）
     */
    private int contextId = 0;

    /**
     * 上次请求的结束时间戳（用来支持限定时间）
     */
    private long lastEndTimestamp;

    /**
     * 上次所处的场景Id
     */
    private Integer lastSceneId;

    /**
     * 上次所处的意图ID
     */
    private Integer lastIntentId;

    /**
     * 上次所处的规则ID
     */
    private Integer lastRuleId;

    /**
     * 缺失的实体集合ID（！！！！！！！！！！！！！！！！！看看后面是用缺失实体对象还是缺失实体ID）
     */
    private Set<Integer> lackingEntitySet;

    /**
     * 积累下来的询问次数（用来支持限定次数）
     */
    private int accumulatedQueryCount = 0;

    /**
     * 上次请求产生的状态
     */
    private int lastState = 0;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public List<Integer> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<Integer> accountIds) {
        this.accountIds = accountIds;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isCanBreakContext() {
        return canBreakContext;
    }

    public void setCanBreakContext(boolean canBreakContext) {
        this.canBreakContext = canBreakContext;
    }

    public boolean isCanBatchQuery() {
        return canBatchQuery;
    }

    public void setCanBatchQuery(boolean canBatchQuery) {
        this.canBatchQuery = canBatchQuery;
    }

    public int getRuleMaxQueryCount() {
        return ruleMaxQueryCount;
    }

    public void setRuleMaxQueryCount(int ruleMaxQueryCount) {
        this.ruleMaxQueryCount = ruleMaxQueryCount;
    }

    public int getEntityMaxQueryCount() {
        return entityMaxQueryCount;
    }

    public void setEntityMaxQueryCount(int entityMaxQueryCount) {
        this.entityMaxQueryCount = entityMaxQueryCount;
    }

    public int getContextWaitTime() {
        return contextWaitTime;
    }

    public void setContextWaitTime(int contextWaitTime) {
        this.contextWaitTime = contextWaitTime;
    }

    public double getAccuracyThreshold() {
        return accuracyThreshold;
    }

    public void setAccuracyThreshold(double accuracyThreshold) {
        this.accuracyThreshold = accuracyThreshold;
    }

    public int getContextId() {
        return contextId;
    }

    public void setContextId(int contextId) {
        this.contextId = contextId;
    }

    public long getLastEndTimestamp() {
        return lastEndTimestamp;
    }

    public void setLastEndTimestamp(long lastEndTimestamp) {
        this.lastEndTimestamp = lastEndTimestamp;
    }

    public Integer getLastSceneId() {
        return lastSceneId;
    }

    public void setLastSceneId(Integer lastSceneId) {
        this.lastSceneId = lastSceneId;
    }

    public Integer getLastIntentId() {
        return lastIntentId;
    }

    public void setLastIntentId(Integer lastIntentId) {
        this.lastIntentId = lastIntentId;
    }

    public Integer getLastRuleId() {
        return lastRuleId;
    }

    public void setLastRuleId(Integer lastRuleId) {
        this.lastRuleId = lastRuleId;
    }

    public Set<Integer> getLackingEntitySet() {
        return lackingEntitySet;
    }

    public void setLackingEntitySet(Set<Integer> lackingEntitySet) {
        this.lackingEntitySet = lackingEntitySet;
    }

    public int getAccumulatedQueryCount() {
        return accumulatedQueryCount;
    }

    public void setAccumulatedQueryCount(int accumulatedQueryCount) {
        this.accumulatedQueryCount = accumulatedQueryCount;
    }

    public int getShortConversationMemorizedCount() {
        return shortConversationMemorizedCount;
    }

    public void setShortConversationMemorizedCount(int shortConversationMemorizedCount) {
        this.shortConversationMemorizedCount = shortConversationMemorizedCount;
    }

    public int getLongConversationMemorizedCount() {
        return longConversationMemorizedCount;
    }

    public void setLongConversationMemorizedCount(int longConversationMemorizedCount) {
        this.longConversationMemorizedCount = longConversationMemorizedCount;
    }

    public int getProcessLogType() {
        return processLogType;
    }

    public void setProcessLogType(int processLogType) {
        this.processLogType = processLogType;
    }

    public int getCalculationLogType() {
        return calculationLogType;
    }

    public void setCalculationLogType(int calculationLogType) {
        this.calculationLogType = calculationLogType;
    }

    public int getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(int algorithmType) {
        this.algorithmType = algorithmType;
    }

    public int getLastState() {
        return lastState;
    }

    public void setLastState(int lastState) {
        this.lastState = lastState;
    }

    @Override
    public String toString() {
        return "SemanticParserRequest{" +
                "cmd='" + cmd + '\'' +
                ", accountIds=" + accountIds +
                ", password='" + password + '\'' +
                ", userId=" + userId +
                ", shortConversationMemorizedCount=" + shortConversationMemorizedCount +
                ", longConversationMemorizedCount=" + longConversationMemorizedCount +
                ", canBreakContext=" + canBreakContext +
                ", canBatchQuery=" + canBatchQuery +
                ", ruleMaxQueryCount=" + ruleMaxQueryCount +
                ", entityMaxQueryCount=" + entityMaxQueryCount +
                ", contextWaitTime=" + contextWaitTime +
                ", accuracyThreshold=" + accuracyThreshold +
                ", processLogType=" + processLogType +
                ", calculationLogType=" + calculationLogType +
                ", algorithmType=" + algorithmType +
                ", contextId=" + contextId +
                ", lastEndTimestamp=" + lastEndTimestamp +
                ", lastSceneId=" + lastSceneId +
                ", lastIntentId=" + lastIntentId +
                ", lastRuleId=" + lastRuleId +
                ", lackingEntitySet=" + lackingEntitySet +
                ", accumulatedQueryCount=" + accumulatedQueryCount +
                ", lastState=" + lastState +
                '}';
    }
}
