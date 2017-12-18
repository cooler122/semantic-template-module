package com.cooler.semantic.client;

import com.cooler.semantic.api.SemanticParserFacade;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class SemanticParserClient {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext-consumer.xml");
    private static SemanticParserFacade semanticParserFacade = (SemanticParserFacade)context.getBean("semanticParserFacade", SemanticParserFacade.class);

    public static void main(String[] args) throws IOException {
        semanticParserFacade.semanticParse(null);
        System.out.println("here!");
    }
}
