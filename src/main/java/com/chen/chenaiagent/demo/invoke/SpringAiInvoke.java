//package com.chen.chenaiagent.demo.invoke;
//
//import jakarta.annotation.Resource;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
///**
// * Spring Ai 框架调用 Ai 大模型
// */
//@Component
//public class SpringAiInvoke implements CommandLineRunner {
//    @Resource
//    private ChatModel dashscopeChatModel;
//
//    @Override
//    public void run(String... args) throws Exception {
//        String call = dashscopeChatModel.call("你好，我是程序员小陈");
//        System.out.println(call);
//
//    }
//}
