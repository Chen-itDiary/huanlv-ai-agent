# 寰旅智行 AI —— 企业级 AI 旅行规划智能体

基于 **Spring AI + 自研分层智能体架构** 的一站式旅行规划 AI 应用。支持多轮对话、会话记忆持久化、RAG 知识库检索，智能体可基于 ReAct 模式自主规划并调用工具完成复杂任务（联网搜索、景点查询、行程文档生成等）。

## ✨ 核心特性

### 1. 分层 AI 智能体架构（参考 OpenManus）

```
ChenManus（具体智能体实例）
   └── ReActAgent（思考-行动解耦的 ReAct 循环）
         └── ToolCallAgent（工具调用决策与执行）
               └── BaseAgent（状态机驱动的 step 循环 + 会话记忆管理）
```

- `BaseAgent`：维护 `AgentState` 状态机（IDLE / RUNNING / FINISHED / ERROR），以 step 循环驱动执行并限制最大步数，防止无限调用
- `ReActAgent`：将"思考（think）"与"行动（act）"解耦，每一步先决策是否调用工具、再执行调用
- `ToolCallAgent`：统一封装 Tool Calling 的请求与结果解析，**新增工具只需注册 ToolCallback 即可接入，零侵入核心流程**

### 2. 全流程 RAG 知识库

- 基于 `TokenTextSplitter` 完成旅游知识文档的向量化切片入库
- `KeywordMetadataEnricher` 自动提取文档关键元信息，支持精确检索
- 自研 `RagAdvisorFactory` 封装 QuestionAnswerAdvisor 构建流程，检索增强与业务解耦
- 同时支持**本地向量库（PgVector）**与**阿里云百炼云端知识库**两种部署模式

### 3. Tool Calling 工具集

| 工具 | 能力 |
|---|---|
| WebSearchTool | 联网搜索（SearchAPI） |
| WebScrapingTool | 网页内容抓取（jsoup） |
| FileOperationTool | 文件读写 |
| PDFGenerationTool | 行程文档 PDF 生成（iText） |
| ResourceDownloadTool | 资源下载 |
| TerminalOperationTool | 终端命令执行 |
| TerminateTool | 任务终止信号 |

### 4. MCP 双服务集成

- **客户端**：以本地 Stdio 方式集成高德地图 MCP 服务，AI 可根据真实地理位置给出景点与路线建议
- **服务端**：基于 Spring AI MCP Server 自研图片搜索 MCP 服务（集成 Pexels API），同时实现 **Stdio 与 SSE 两套传输模式**，适配本地进程调用与远程部署两种场景

### 5. SSE 流式响应

基于 `SseEmitter` 实现 AI 推理的流式输出接口，结合 `CompletableFuture` 将推理任务异步化，避免长任务阻塞 Tomcat 工作线程，推理过程实时推送前端。

### 6. 会话记忆

`ChatMemory` 多轮对话记忆 + 文件级持久化，会话间相互隔离，支持记忆检索回放。

## 🛠 技术栈

Spring Boot 3 / Spring AI 1.0 / LangChain4j / PgVector / 阿里云百炼（Qwen-plus）/ MCP（Stdio + SSE）/ jsoup / iText / Vue3 + Vite（前端）

## 🚀 快速启动

```bash
# 1. 配置密钥（环境变量注入，避免硬编码）
export DASHSCOPE_API_KEY=你的灵积APIKey
export SEARCH_API_KEY=你的SearchAPI密钥

# 2. 启动后端（需先准备 PgVector 数据库）
mvn spring-boot:run

# 3. 启动前端
cd ai-chat-frontend && npm install && npm run dev
```

> 配置文件说明：`application-prod.yml` 为默认激活配置（密钥均从环境变量读取）；本地调试可将真实密钥写入 `application-local.yml`（已在 .gitignore 中排除，不会入库）。

## 📂 项目结构

```
src/main/java/com/chen/chenaiagent
├── agent/        # 分层智能体（BaseAgent / ReActAgent / ToolCallAgent / ChenManus）
├── advisor/      # 自定义 Advisor（日志、Re-Reading）
├── app/          # 业务应用（LoveApp 旅行助手）
├── chatmemory/   # 会话记忆持久化
├── controller/   # 接口层（SSE 流式）
├── rag/          # RAG 全流程（加载、切片、向量库、查询增强）
├── tools/        # Tool Calling 工具集
└── demo/invoke/  # 三种大模型调用方式对比（SDK / Spring AI / LangChain4j）
```
