package com.chen.chenaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 自定义过滤表达式的 查询增强顾问
 */
public class LoveAppCustomizedRagAdvisorFactory {
    public static Advisor createAdvisor(VectorStore vectorStore, String filterCondition){
        Filter.Expression expression = new FilterExpressionBuilder().eq("filename", filterCondition).build();
        VectorStoreDocumentRetriever vectorStoreDocumentRetriever = VectorStoreDocumentRetriever.builder().topK(3).filterExpression(expression).vectorStore(vectorStore).similarityThreshold(0.5).build();
        return RetrievalAugmentationAdvisor.builder().documentRetriever(vectorStoreDocumentRetriever).queryAugmenter(ContextualQueryAugmenter.builder().emptyContextPromptTemplate(new PromptTemplate("该问答不在我的知识库范围内，请回答给用户：“抱歉，我无法回答您的问题”")).build()).build();
    }

}
