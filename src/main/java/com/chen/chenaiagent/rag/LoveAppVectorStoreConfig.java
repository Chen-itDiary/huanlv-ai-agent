//package com.chen.chenaiagent.rag;
//
//import com.chen.chenaiagent.spliter.MyTokenTextSplitter;
//import jakarta.annotation.Resource;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.embedding.EmbeddingModel;
//import org.springframework.ai.vectorstore.SimpleVectorStore;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class LoveAppVectorStoreConfig {
//
//    @Resource
//    private LoveAppDocumentLoader loveAppDocumentLoader;
//
//    @Resource
//    private MyKeywordEnricher myKeywordEnricher;
//
//    @Resource
//    private MyTokenTextSplitter myTokenTextSplitter;
//    @Bean
//    public VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel){
//        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
//        List<Document> documentList = loveAppDocumentLoader.loadMarkdownDocuments();
////        List<Document> documentList1 = myTokenTextSplitter.splitDocuments(documentList);
////        List<Document> documents = myKeywordEnricher.keywordEnrich(documentList);
//        simpleVectorStore.add(documentList);
//        return simpleVectorStore;
//    }
//}
