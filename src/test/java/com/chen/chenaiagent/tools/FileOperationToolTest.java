package com.chen.chenaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String filename = "编程日记.txt";
        String result = fileOperationTool.readFile(filename);
        assertNotNull(result);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String filename = "编程日记.txt";
        String content = "今天是2021年11月23日";
        String result = fileOperationTool.writeFile(filename, content);
        assertNotNull(result);
    }
}