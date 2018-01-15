package com.cooler.semantic.client;

import com.cooler.semantic.facade.SemanticParserFacade;
import com.cooler.semantic.entity.SemanticParserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Arrays;

public class SemanticParserClient {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext-consumer.xml");
    private static SemanticParserFacade semanticParserFacade = (SemanticParserFacade)context.getBean("semanticParserFacade", SemanticParserFacade.class);
    private static Logger logger = LoggerFactory.getLogger(SemanticParserClient.class.getName());

    public static void main(String[] args) throws IOException {

        Integer contextId = 1;

        logger.info("客户端，开始访问...");
        SemanticParserRequest request = new SemanticParserRequest();
        request.setCmd("北京天气怎么样？");                   //添加一个句子
        request.setAccountIds(Arrays.asList(1));                     //添加一个测试账户
        request.setPassword("123456");
        request.setUserId(2);                                       //添加一个用户标号
        request.setContextId(contextId ++);                                    //添加一个上下文编号
        semanticParserFacade.semanticParse(request);

        int state = -1;

        SemanticParserRequest request2 = new SemanticParserRequest();
        request2.setCmd("今天");                   //添加一个句子
        request2.setAccountIds(Arrays.asList(1));                     //添加一个测试账户
        request2.setPassword("123456");
        request2.setUserId(2);                                       //添加一个用户标号
        request2.setContextId(contextId ++);                                    //添加一个上下文编号
        request2.setLastState(state);
        semanticParserFacade.semanticParse(request2);

//        SemanticParserRequest request3 = new SemanticParserRequest();
//        request3.setCmd("它的稻花香");                   //添加一个句子
//        request3.setAccountIds(Arrays.asList(1));                     //添加一个测试账户
//        request3.setPassword("123456");
//        request3.setUserId(2);                                       //添加一个用户标号
//        request3.setContextId(1);                                    //添加一个上下文编号
//
//        semanticParserFacade.semanticParse(request3);
    }
}
