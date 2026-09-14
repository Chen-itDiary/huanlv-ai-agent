package com.chen.chenaiagent.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@Slf4j
@SpringBootTest
public class PostgreSQLVectorStoreLoaderTest {
    @Resource
    private VectorStore vectorStore;
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;
    @Test
    void load() {
        List<Document> documentList = loveAppDocumentLoader.loadMarkdownDocuments();
        log.info("documentList:{}", documentList);
        for(int i = 0; i < documentList.size(); i += 10){
            int endIndex = Math.min(i + 10, documentList.size());
            List<Document> documents = documentList.subList(i, endIndex);
            vectorStore.add(documents);
        }
    }
}
