package com.chen.chenaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.AssertionsKt.assertNotNull;

public class WebScrapingToolTest {

    @Test
    public void testScrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://www.codefather.cn";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
    }
}
