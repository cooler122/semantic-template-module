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
//        String[] sentences = { "北京今天天气怎么样？" };                                                                 //测试全参匹配

//        String[] sentences = { "天气怎么样？", "今天", "北京" };                                                         //测试缺参匹配

//        String[] sentences = { "北京今天天气怎么样？", "上海呢？", "天津呢？" };                                           //测试全参情况下的换参匹配

//        String[] sentences = { "北京天气怎么样？", "上海呢？", "天津呢？", "今天"};                                          //测试缺参情况下的换参匹配


//        String[] sentences = { "天气怎么样？", "哈哈", "哈哈", "哈哈", "今天", "北京" };                                  //测试打断上下文的缺参匹配1

//        String[] sentences = { "天气怎么样？", "哈哈", "哈哈",  "今天", "哈哈", "哈哈", "北京" };                          //测试打断上下文的缺参匹配2

//        String[] sentences = {  "哈哈", "哈哈",  "今天", "哈哈", "哈哈", "北京" };                                       //测试匹配失败

//        String[] sentences = {  "唱周杰伦的双节棍" };                                                                   //另一场景，测试全参匹配

//        String[] sentences = {  "唱周杰伦的听妈妈的话" };                                                                //另一场景，测试全参匹配（自定义分词）

//        String[] sentences = {  "唱周杰伦的歌" , "听妈妈的话" };                                                         //另一场景，测试缺参匹配（自定义分词）

        String[] sentences = {  "唱周杰伦的歌" , "唱他的双节棍", "播放他的听妈妈的话", "唱林俊杰的江南", "唱他的美人鱼"};       //另一场景，测试缺参匹配（自定义分词 + 指代消解）

        Integer contextId = 1;
        Integer state = 0;
        for (String sentence : sentences) {
            SemanticParserResponse semanticParserResponse = buildRequest(sentence, contextId, state);
            state = semanticParserResponse.getState();

            contextId ++;
            System.out.println("人   ------    " + sentence);
            System.out.println("机   ------    " + semanticParserResponse.getResponseMsg());
            System.out.println(JSON.toJSONString(semanticParserResponse));
        }
    }

    public static SemanticParserResponse buildRequest(String sentence, Integer contextId, Integer state){
        SemanticParserRequest request = new SemanticParserRequest();
        request.setCmd(sentence);                                                                                       //添加一个句子
        request.setAccountIds(Arrays.asList(1));                                                                        //添加一个测试账户
        request.setPassword("123456");
        request.setUserId(2);                                                                                           //添加一个用户标号
        request.setContextId(contextId);                                                                                //添加一个上下文编号
        request.setLastState(state);
        return semanticParserFacade.semanticParse(request);
    }
}
