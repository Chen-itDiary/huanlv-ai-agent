package com.chen.chenaiagent.app;

import com.chen.chenaiagent.advisor.MyLoggerAdvisor;
import com.chen.chenaiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@Slf4j
public class LoveApp {

//    @Resource
    private VectorStore loveAppVectorStore;

//    @Resource
//    private VectorStore vectorStore;
    @Resource
    private Advisor loveAppRagCloudAdvisor;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    private final ChatClient chatClient; // 千问
    private final String SYSTEM_TEXT = "你是一位资深的AI旅游大师，拥有10年以上全球旅游规划、目的地深度解读、行程定制的专业经验，熟悉不同地区的文化习俗、交通规则、美食特色、避坑指南和小众玩法。\n" +
            "\n" +
            "### 核心职责\n" +
            "1. 行程规划：根据用户的出行时间、预算、人数、出行偏好（亲子/情侣/户外/休闲/人文）、出发地、目的地，定制详细到每天的行程表（含交通方式、景点推荐、游玩时长、用餐建议）；\n" +
            "2. 目的地解答：精准回答用户关于目的地的所有问题（景点开放时间/门票价格、当地美食推荐、住宿选择、气候/穿搭建议、安全注意事项、签证/证件要求、货币/支付方式）；\n" +
            "3. 个性化调整：根据用户的特殊需求（如小众路线、素食偏好、无障碍出行、宠物友好、摄影打卡）优化行程，规避热门坑点（如高价宰客、虚假宣传的景点）；\n" +
            "4. 应急建议：提供旅行中常见问题的解决方案（如丢行李、突发疾病、语言不通、天气突变、交通延误）；\n" +
            "5. 文化适配：介绍目的地的文化禁忌、礼仪规范，避免用户因文化差异产生不便。\n" +
            "\n" +
            "### 回答要求\n" +
            "1. 实用性优先：所有推荐的信息（如景点、餐厅、交通）需具体可落地，避免空泛（例：推荐“XX路XX号的XX小吃店”而非“当地有很多特色小吃”）；\n" +
            "2. 语言通俗：避免专业术语，用用户易懂的口语化表达，同时保持专业度；\n" +
            "3. 结构清晰：复杂行程/建议分点列出，重点信息（如必带物品、避坑提示）用加粗/标注突出；\n" +
            "4. 主动补全：用户提问不完整时，主动追问关键信息（如“请问你的出行时间是几天？预算大概在多少？”）；\n" +
            "5. 本地化适配：针对国内/国外目的地，分别提供符合当地实际的建议（如国内推荐微信支付/高德地图，国外推荐Google Maps/信用卡）；\n" +
            "6. 拒绝误导：不确定的信息需明确说明（如“该景点的开放时间暂未更新，建议出行前电话确认”），不编造信息。\n" +
            "\n" +
            "### 禁止行为\n" +
            "1. 不推荐违规/危险的玩法（如未开发的野景区、无证黑车）；\n" +
            "2. 不夸大宣传（如“绝对不踩坑”“性价比最高”等绝对化表述）；\n" +
            "3. 不涉及政治/敏感话题，聚焦旅游本身；\n" +
            "4. 不提供虚假价格/时效信息，所有数据以最新公开信息为准。\n" +
            "\n" +
            "现在，请以友好、专业的语气回应用户的旅游相关问题，成为用户的专属旅游顾问，另外请注意，你回复内容的格式不必以markdown格式，按照优美的排版即可。";
    /**
     * 初始化 Ai 客户端
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel){
        // 初始化基于内存的 chatMemory
        InMemoryChatMemoryRepository inMemoryChatMemoryRepository = new InMemoryChatMemoryRepository();
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(inMemoryChatMemoryRepository).build();
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_TEXT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build()
                        // 自定义 log日志拦截器
//                        MyLoggerAdvisor.builder().build())
//                        new ReReadingAdvisor())
                ).build();
    }

    /**
     * 基础版 Ai 对话（支持多轮对话）
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}", content);
        return content;
    }

    /**
     * 基础版 Ai 对话（支持多轮对话，SSE流式输出）
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatWithSSE(String message, String chatId) {
        ToolCallback[] toolCallbacks = toolCallbackProvider.getToolCallbacks();
        toolCallbacks = ArrayUtils.addAll(toolCallbacks, allTools);
        Flux<String> content = chatClient
                .prompt()
                .user(message)
                .toolCallbacks(toolCallbacks)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(MyLoggerAdvisor.builder().build())
                .stream()
                .content();
        log.info("content:{}", content);
        return content;
    }
    record LoveReport(String title, List<String> suggestions){

    }
//    /**
//     * 基础版 Ai 对话（生成恋爱报告）
//     * @param message
//     * @param chatId
//     * @return
//     */
//    public LoveReport doChatWithReport(String message, String chatId) {
//        LoveReport loveReport = chatClient
//                .prompt()
//                .system(SYSTEM_TEXT + "每次对话后都要生成恋爱结果，名称为{用户名}的恋爱报告，内容为建议列表")
//                .user(message)
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
//                .call()
//                .entity(LoveReport.class);
//        log.info("loveReport: {}", loveReport);
//        return loveReport;
//    }

    /**
     * Ai 恋爱大师向量数据库回答
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithVectorStore(String message, String chatId) {
//        Advisor advisor = LoveAppCustomizedRagAdvisorFactory.createAdvisor(vectorStore, "恋爱常见问题和回答 - 已婚篇.md");
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(MyLoggerAdvisor.builder().build())
                // 基于向量数据库的问答
//                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
//                // 向量数据库的检索增强（基于云知识库服务）
//                .advisors(loveAppRagCloudAdvisor)
//                .advisors(advisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}", content);
        return content;
    }

    /**
     * 基础版 Ai 对话（生成恋爱报告）
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTools(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .toolCallbacks(allTools)
                .toolNames()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(MyLoggerAdvisor.builder().build())
                .call()
                        .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}", content);
        return content;
    }

    /**
     * 基础版 Ai 对话（调用MCP服务）
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMCP(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .toolCallbacks(toolCallbackProvider)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(MyLoggerAdvisor.builder().build())
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}", content);
        return content;
    }


}
