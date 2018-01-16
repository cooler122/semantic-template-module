package com.cooler.semantic.client;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.entity.SemanticParserResponse;
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

    public static void main(String[] args) throws IOException {
        String[] sentences = {
                "天气怎么样？",
                "今天",
                "北京"
        };

        Integer contextId = 1;
        Integer state = 0;
        for (String sentence : sentences) {
            SemanticParserResponse semanticParserResponse = buildRequest(sentence, contextId, state);
            state = semanticParserResponse.getState();

            contextId ++;
            System.out.println("人   -------------------    " + sentence);
            System.out.println("机   -------------------    " + semanticParserResponse.getResponseMsg());
            System.out.println(JSON.toJSONString(semanticParserResponse));
        }
    }

    public static SemanticParserResponse buildRequest(String sentence, Integer contextId, Integer state){
        SemanticParserRequest request = new SemanticParserRequest();
        request.setCmd(sentence);                   //添加一个句子
        request.setAccountIds(Arrays.asList(1));                     //添加一个测试账户
        request.setPassword("123456");
        request.setUserId(2);                                       //添加一个用户标号
        request.setContextId(contextId);                                    //添加一个上下文编号
        request.setLastState(state);
        return semanticParserFacade.semanticParse(request);
    }
}
