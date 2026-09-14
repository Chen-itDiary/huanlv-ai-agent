package com.chen.chenaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.vectorstore.VectorStore;

import static org.junit.jupiter.api.Assertions.*;

class LoveAppCustomizedRagAdvisorFactoryTest {

    @Resource
    private VectorStore vectorStore;
    @Test
    void createAdvisor() {
        RetrievalAugmentationAdvisor advisor = (RetrievalAugmentationAdvisor) LoveAppCustomizedRagAdvisorFactory.createAdvisor(vectorStore, "恋爱常见问题和回答 - 单身篇.md");
    }
}