package com.chen.chenaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

public class LangChainAiInvoke {
    public static void main(String[] args) {
        // API Key 通过环境变量注入，避免密钥硬编码入库：DASHSCOPE_API_KEY
        ChatLanguageModel chatModel = QwenChatModel.builder()
                .apiKey(System.getenv().getOrDefault("DASHSCOPE_API_KEY", "your-dashscope-api-key"))
                .modelName("qwen-max").build();
        String answer = chatModel.chat("你好，我是程序员小陈");
        System.out.println(answer);
    }
}
