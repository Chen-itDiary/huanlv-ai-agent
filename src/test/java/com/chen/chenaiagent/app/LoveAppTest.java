package com.chen.chenaiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Slf4j
@SpringBootTest
class LoveAppTest {
    @Resource
    private LoveApp loveApp;
    @Resource
    VectorStore vectorStore;
    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        String message1 = "我的职业是 程序员";
        loveApp.doChat(message1, chatId);
        String message2 = "你好,我的另一半是小皮";
        loveApp.doChat(message2, chatId);
        String message3 = "请帮我回忆一下我的职业是什么?";
        loveApp.doChat(message3, chatId);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport("我的名字是小陈，我想让我的另一半（小皮）更加爱我，我不知道该怎么做。", chatId);
        Assertions.assertNotNull(loveReport);

    }

    @Test
    void doChatWithVectorStore() {
        String chatId = UUID.randomUUID().toString();
        String content = loveApp.doChatWithVectorStore("我目前单身，如何找到伴侣", chatId);
        Assertions.assertNotNull(content);
    }

    @Test
    void postgreSqlVectorStore() {
//        List<Document> documents = List.of(
//                new Document("我是程序员小陈，我非常喜欢编程", Map.of("meta1", "meta1")),
//                new Document("小陈是个非常帅气逼人的人"),
//                new Document("我期待生活，我相信生活会越来越好", Map.of("meta2", "meta2")));
//        vectorStore.add(documents);
        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder().similarityThreshold(0.5).query("我喜欢").topK(5).build());
        log.info("results: {}", results);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？");

        // 测试网页抓取：恋爱案例分析
        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");

        // 测试终端操作：执行代码
        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的恋爱档案为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMCP() {
//        String chatId = UUID.randomUUID().toString();
//        String answer = loveApp.doChatWithMCP("我的另一半住在上海静安区，请帮我推荐五个5公里范围内的约会地点", chatId);
//        Assertions.assertNotNull(answer);
        // 测试图片搜索mcp
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithMCP("帮我搜索一些玫瑰的图片", chatId);
        Assertions.assertNotNull(answer);
    }
}