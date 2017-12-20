package com.cooler.semantic.client;

import com.cooler.semantic.api.SemanticParserFacade;
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

        logger.info("客户端，开始访问...");
        SemanticParserRequest request = new SemanticParserRequest();
        request.setCmd("北京今天天气怎么样？");                   //添加一个句子
        request.setAccountId(Arrays.asList(1));                     //添加一个测试用户

        semanticParserFacade.semanticParse(request);
    }
}
