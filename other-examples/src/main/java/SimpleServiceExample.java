import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

public class SimpleServiceExample {

    interface Assistant {

        String chat(String message);
    }

    public static void main(String[] args) {

        ChatModel chatModel = OpenAiChatModel.builder()
                .baseUrl("http://localhost:8085/v1")
                .apiKey("unused")
                .modelName("Qwen2.5-VL-3B-Custom")
                .build();

        Assistant assistant = AiServices.create(Assistant.class, chatModel);

        String answer = assistant.chat("你好");

        System.out.println(answer); // Hello! How can I assist you today?
    }
}
