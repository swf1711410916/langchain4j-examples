import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;

import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

public class ChatMemoryExamples {

    /**
     * This example demonstrates how to use a low-level {@link ChatMemory} API.
     * For a high-level API with AI Services see {@link ServiceWithMemoryExample}.
     */

    public static void main(String[] args) {
        //OpenAiTokenCountEstimator：用于估算每条消息占用的 Token 数量的工具
        //MessageWindowChatMemory.withMaxMessages(10)：按条数截断（只保留最近 10 条对话）
        //TokenWindowChatMemory.withMaxTokens(300, ...)：按容量/Token 数截断（推荐在生产环境使用，能精准防止上下文溢出）
        // 自定义模型这里不能用这个函数计算token,将 "Qwen2.5-VL-3B-Custom" 替换为标准的 OpenAI 模型名（如 "gpt-3.5-turbo" 或 "gpt-4"）
        ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(300, new OpenAiTokenCountEstimator("gpt-3.5-turbo"));

        ChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://localhost:8085/v1")
                .apiKey("unused")
                .modelName("Qwen2.5-VL-3B-Custom")
                .build();

        // You have full control over the chat memory.
        // You can decide if you want to add a particular message to the memory
        // (e.g. you might not want to store few-shot examples to save on tokens).
        // You can process/modify the message before saving if required.
        // 1. 手动把用户输入存入 ChatMemory 记忆库
        chatMemory.add(userMessage("你好, 我的名字是秦始皇"));
        // 2. 将 ChatMemory 中保存的所有历史消息打包传给 model.chat(...)
        //chatMemory.messages()：获取当前记忆库中存留的所有消息列表。此时列表里只有 1 条消息：User: 你好, 我的名字是秦始皇
        AiMessage answer = model.chat(chatMemory.messages()).aiMessage();
        System.out.println(answer.text()); // Hello Klaus! How can I assist you today?
        // 3. 必须把 AI 的回复也手动加入 ChatMemory，这样后续才能构成完整问答对
        //chatMemory.add(answer)：AI 回复后，必须手动把 AI 的回答存入记忆库。此时记忆库里有 2 条消息（1 条 User + 1 条 AI）
        chatMemory.add(answer);

        chatMemory.add(userMessage("我的名字是什么?"));
        AiMessage answerWithName = model.chat(chatMemory.messages()).aiMessage();
        System.out.println(answerWithName.text()); // Your name is Klaus.
        chatMemory.add(answerWithName);
    }
}
