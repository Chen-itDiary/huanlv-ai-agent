package com.chen.chenaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebSearchToolTest {

    @Value("${spring.ai.search-api.api-key}")
    private String searchApKey;
    @Test
    void searchWeb() {
        String result = new WebSearchTool(searchApKey).searchWeb("程序员鱼皮");
        assertNotNull(result);
    }
}