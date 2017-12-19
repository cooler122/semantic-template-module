package com.cooler.semantic.client;

import com.cooler.semantic.api.SemanticParserFacade;
import com.cooler.semantic.entity.SemanticParserRequest;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class SemanticParserClient {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext-consumer.xml");
    private static SemanticParserFacade semanticParserFacade = (SemanticParserFacade)context.getBean("semanticParserFacade", SemanticParserFacade.class);

    public static void main(String[] args) throws IOException {
        SemanticParserRequest request = new SemanticParserRequest();
        request.setCmd("北京今天天气怎么样？");

        semanticParserFacade.semanticParse(request);
        System.out.println("here!");
    }
}
