package com.cooler.semantic.client;

import com.alibaba.fastjson.JSON;
import com.cooler.semantic.entity.SemanticParserResponse;
import com.cooler.semantic.facade.SemanticParserFacade;
import com.cooler.semantic.entity.SemanticParserRequest;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Arrays;

public class SemanticParserClient {
    private static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext-consumer.xml");
    private static SemanticParserFacade semanticParserFacade = (SemanticParserFacade)context.getBean("semanticParserFacade", SemanticParserFacade.class);

    public static void main(String[] args) throws IOException {
//        String[] sentences = { "今天北京天气怎么样？" };                                                              //测试全参匹配

//        String[] sentences = { "天气怎么样？", "今天", "北京" };                                                      //测试缺参匹配

//        String[] sentences = { "天气怎么样？", "今天", "北京", "天气怎么样？", "今天北京" };                          //测试缺参匹配（批量询问，需要设置can_batch_query = 1）

//        String[] sentences = { "天气怎么样？", "我说的是今天", "当然是北京", "天气怎么样？", "今天和北京" };                          //测试有冗余词汇的缺参匹配（批量询问，需要设置can_batch_query = 1）

//        String[] sentences = { "北京今天天气怎么样？", "上海呢？", "天津呢？" , "明天呢？", "广州天气呢？", "广州今天的", "我还想知道北京的"};     //测试全参情况下的换参匹配

//        String[] sentences = { "北京天气怎么样？", "上海呢？", "天津呢？", "今天"};                                        //测试缺参情况下的换参匹配

        String[] sentences = { "天气怎么样？", "哈哈", "哈哈", "哈哈", "今天", "北京" };                                   //测试打断上下文的缺参匹配1

//        String[] sentences = { "天气怎么样？", "哈哈", "哈哈",  "今天", "哈哈", "哈哈", "北京", "哈哈"};                       //测试打断上下文的缺参匹配2

//        String[] sentences = {  "哈哈", "哈哈",  "今天", "哈哈", "哈哈", "北京" };                                       //测试匹配失败

//        String[] sentences = {  "唱周杰伦的双节棍" };                                                                   //另一场景，测试全参匹配

//        String[] sentences = {  "唱周杰伦的听妈妈的话" };                                                                //另一场景，测试全参匹配（自定义分词）

//        String[] sentences = {  "唱周杰伦的歌" , "听妈妈的话" };                                                         //另一场景，测试缺参匹配（自定义分词）

//        String[] sentences = {  "唱林俊杰的歌", "美人鱼", "江南"};                                                       //另一场景，测试换参匹配（连续指代消解 + 换参匹配）

//        String[] sentences = {  "唱林俊杰的歌", "唱他的美人鱼", "江南"};                                                       //另一场景，测试失败情况下的指代消解（指代消解 + 换参匹配）

//        String[] sentences = {  "唱周杰伦的歌" , "唱他的双节棍", "播放他的听妈妈的话", "唱林俊杰的歌", "美人鱼", "江南"};        //另一场景，测试换参匹配（自定义分词 + 连续指代消解 + 换参匹配）

//        String[] sentences = {  "唱周杰伦的歌" , "唱他的双节棍", "播放他的听妈妈的话", "唱林俊杰的美人鱼", "江南"};        //另一场景，测试换参匹配（自定义分词 + 连续指代消解 + 换参匹配）

//        String[] sentences = { "天气怎么样？", "今天", "唱周杰伦的歌" , "唱周杰伦的双节棍", "播放他的听妈妈的话", "林俊杰的江南"};        //测试多场景

//        String[] sentences = { "我要取款", "八八八", "六六六六六六", "三五千", "三千", "确认" , "要"};        //测试多场景

        Integer contextId = (int)(Math.random() * 10000000 + 1);
//        Integer contextId = 21;
        System.out.println("对话编号：" + contextId);

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
        request.setAccountIds(Arrays.asList(1, 2, 3));                                                                        //添加一个测试账户 accountId = 1
        request.setPassword("123456");                    //TODO:看看密码有没有效果
        request.setUserId(2);                                                                                           //添加一个用户标号 userId = 2
        request.setContextId(contextId);                                                                                //添加一个上下文编号
        request.setLastState(state);
        return semanticParserFacade.semanticParse(request);
    }
}
