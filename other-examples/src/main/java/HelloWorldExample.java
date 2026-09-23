import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

public class HelloWorldExample {

    public static void main(String[] args) {

        // Create an instance of a model
        ChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://localhost:8085/v1")
                .apiKey("unused")
                .modelName("Qwen2.5-VL-3B-Custom")
                .logRequests(true)
                .logResponses(true)
                .build();

        // Start interacting
        String answer = model.chat("你好，仔细介绍下自己");

        System.out.println(answer); // Hello! How can I assist you today?
    }
}
