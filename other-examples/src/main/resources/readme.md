在 `other-examples` 模块中，包含了从**低阶底层 API（Low-Level API）** 到 **高阶 Agent 声明（AiServices）**，再到 **RAG 与工具调用** 的完整练习代码。

推荐按照以下 **4 个阶段** 循序渐进地运行和学习：

---

### 第一阶段：打基础（底层模型交互与基础组件）

先学习如何直接调用大模型，以及如何使用 Prompt 模板和低阶记忆组件。

1. **`HelloWorldExample`**
* **作用**：最简单的单轮对话，验证模型网络连通性与配置。




2. **`StreamingExamples`**
* **作用**：学习流式输出（ 打字机效果），掌握 `StreamingChatModel`。


3. **`PromptTemplateExamples`** / **`StructuredPromptTemplateExamples`**
* **作用**：学习通过变量拼接 Prompt，以及定义结构化提示词模板。




4. **`ChatMemoryExamples`**
* **作用**：理解大模型如何维持多轮对话上下文（消息历史的添加与管理）。



---

### 第二阶段：进阶高阶 API（声明式 AI 服务 `AiServices`）

LangChain4j 的核心精髓是 `AiServices`，用接口和注解快速定义智能体。

5. **`SimpleServiceExample`**
* **作用**：学习最基础的 `AiServices.create()` 极简写法。




6. **`OtherServiceExamples`**
* **作用**：学习 `@SystemMessage`、`@UserMessage` 注解及结构化输出（将 LLM 返回解析为 Java POJO/对象）。


7. **`ServiceWithMemoryExample`** / **`ServiceWithMemoryForEachUserExample`**
* **作用**：学习在 `AiServices` 中自动集成上下文记忆，以及区分多用户的不同 MemoryId。


8. **`ServiceWithPersistentMemoryExample`** / **`ServiceWithPersistentMemoryForEachUserExample`**
* **作用**：学习将聊天记录持久化保存到数据库或文件（如 MapDB/Redis）中。



---

### 第三阶段：核心 Agent 能力（工具调用与能力扩展）

让智能体获得“调用 Java 方法/外部系统”以及“自控”的能力。

9. **`ServiceWithToolsExamples`**
* **作用**：**必须重点掌握**。学习 `@Tool` 注解，让大模型自主判断并调用本地 Java 方法（Function Calling）。


10. **`ServiceWithDynamicToolsExample`**
* **作用**：学习在运行时动态加载与注册 Tool 工具。




11. **`ServiceWithAutoModerationExample`**
* **作用**：学习在请求模型前后进行敏感词拦截与安全审计。





---

### 第四阶段：RAG 增强检索（知识库与文件外挂）

学习如何把外部文档加载并注入到大模型的对话中。

12. **`DocumentLoaderExamples`**
* **作用**：学习如何解析 txt、pdf、doc 等本地文件并转化为 `Document`。


13. **`embedding/` 文件夹中的类**
* **作用**：学习文本向量化（EmbeddingModel），以及将向量存入向量数据库。


14. **`ServiceWithRetrieverExample`** / **`ChatWithDocumentsExamples`**
* **作用**：学习整合检索器（Retriever），实现完整的本地知识库问答系统。