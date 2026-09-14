package com.chen.chenaiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class LoveAppDocumentLoader {
    private final ResourcePatternResolver resourcePatternResolver;
    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver){
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 加载多篇 Markdown文件
     * @return
     */
   public List<Document> loadMarkdownDocuments(){
       List<Document> documentsList = new ArrayList<>();
       try {
           Resource[]   documentsResources = resourcePatternResolver.getResources("classpath:document/*.md");
       for (Resource documentsResource : documentsResources) {
           String filename = documentsResource.getFilename();

           MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig
                   .builder()
                   .withHorizontalRuleCreateDocument(true)
                   .withAdditionalMetadata("filename", filename)
                   .withIncludeCodeBlock(false)
                   .withIncludeBlockquote(false)
                   .build();
           MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader(documentsResource, config);
           documentsList.addAll(markdownDocumentReader.get());
       }
       } catch (IOException e) {
           log.error("Markdown文件 加载失败...");
       }
       return documentsList;
   }
}
