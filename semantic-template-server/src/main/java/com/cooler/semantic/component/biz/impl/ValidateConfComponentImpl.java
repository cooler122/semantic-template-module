package com.cooler.semantic.component.biz.impl;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.component.biz.FunctionComponentBase;
import com.cooler.semantic.entity.Account;
import com.cooler.semantic.entity.AccountConfiguration;
import com.cooler.semantic.entity.SemanticParserRequest;
import com.cooler.semantic.component.ComponentBizResult;
import com.cooler.semantic.service.internal.AccountConfigurationService;
import com.cooler.semantic.service.internal.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component("validateConfComponent")
public class ValidateConfComponentImpl extends FunctionComponentBase<SemanticParserRequest, SemanticParserRequest> {

    private static Logger logger = LoggerFactory.getLogger(ValidateConfComponentImpl.class.getName());

    @Autowired
    @Qualifier("accountService")
    private AccountService accountService = null;
    @Autowired
    private AccountConfigurationService accountConfigurationService = null;

    public ValidateConfComponentImpl() {
        super("VCC", "SO_1", "semanticParserRequest", "semanticParserRequest");
    }

    @Override
    protected ComponentBizResult<SemanticParserRequest> runBiz(SemanticParserRequest bizData) {
        logger.info("SO_1.校验和配置");

        ComponentBizResult validateResult = validate(bizData);                       //校验请求体
        if (validateResult != null) return validateResult;

        ComponentBizResult checkInResult = checkIn(bizData);                         //账户权限校验
        if (checkInResult != null) return checkInResult;

        setAccountData(bizData);                                                     //设置用户配置参数

        return new ComponentBizResult("VCC_S", 1, bizData);
    }

    private ComponentBizResult validate(SemanticParserRequest request) {
        logger.info("SO-1-1.请求体校验...");
        String cmd = request.getCmd();
        List<Integer> accountIds = request.getAccountIds();
        Integer userId = request.getUserId();
        //request中其它量都有默认值或允许为null
        if (cmd == null || cmd.equals("")) {                                          //传来的句子不能为null或空字符串
            return new ComponentBizResult("VCC_F", "VCC_F1_nullCmd");
        }
        if (accountIds == null || accountIds.size() == 0) {                         //账号不能为null并且里面的元素不少于1
            return new ComponentBizResult("VCC_F", "VCC_F2_nullAccountId");
        }
        if (userId < 0) {                                                          //用户账号不得小于0
            return new ComponentBizResult("VCC_F", "VCC_F3_errorUserId");
        }
        return null;
    }


    private ComponentBizResult checkIn(SemanticParserRequest request) {
        logger.info("SO-1-2.账户权限校验...");
        Integer accountId = request.getAccountIds().get(0);
        String password = request.getPassword();

        Account account = accountService.selectByPrimaryKey(accountId);
        String passwordMd5 = account.getPasswordMd5();

        int contextId = request.getContextId();
        if (contextId % 20 == 0) {                                              //针对每一个账户每20此检查一遍密码是否正确
            try {
                boolean isPassed = EncoderByMd5(password).equals(passwordMd5);
                if (!isPassed){
                    return new ComponentBizResult("VCC_F", "VCC_F4_errorPassword");
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    private void setAccountData(SemanticParserRequest request) {
        logger.info("SO-1-3.账户自定义参数设置...");
        Integer coreAccountId = request.getAccountIds().get(0);
        Integer userId = request.getUserId();

        AccountConfiguration accountConfiguration = accountConfigurationService.selectAIdUId(coreAccountId, userId);
        logger.info(JSON.toJSONString(accountConfiguration));

        if(accountConfiguration != null){
            request.setCanBreakContext(accountConfiguration.getCanBreakContext());
            request.setCanBatchQuery(accountConfiguration.getCanBatchQuery());
            request.setRuleMaxQueryCount(accountConfiguration.getRuleMaxQueryCount());
            request.setEntityMaxQueryCount(accountConfiguration.getEntityMaxQueryCount());
            request.setContextWaitTime(accountConfiguration.getContextWaitTime());
            request.setAccuracyThreshold(accountConfiguration.getAccuracyThreshold());
            request.setLog_type(accountConfiguration.getLogType());
            request.setAlgorithm_type(accountConfiguration.getAlgorithmType());
        }
    }

    public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String password = "123456";

        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String password_md5 = base64en.encode(md5.digest(password.getBytes("utf-8")));
        System.out.println(password_md5);
    }
}
