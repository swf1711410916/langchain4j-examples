import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static java.util.Arrays.asList;

public class OtherServiceExamples {

    static ChatModel chatModel = OpenAiChatModel.builder()
            .baseUrl("http://localhost:8085/v1")
            .apiKey("unused")
            .modelName("Qwen2.5-VL-3B-Custom")
            .build();

    static class Sentiment_Extracting_AI_Service_Example {

        enum Sentiment {
            //积极，中性，消极
            POSITIVE, NEUTRAL, NEGATIVE;
        }

        interface SentimentAnalyzer {

            @UserMessage("分析情感从 {{it}}")
            Sentiment analyzeSentimentOf(String text);

            @UserMessage("对于 {{it}} 是否是一个消极的情感?")
            boolean isPositive(String text);
        }

        public static void main(String[] args) {

            SentimentAnalyzer sentimentAnalyzer = AiServices.create(SentimentAnalyzer.class, chatModel);

            Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf("愤怒!");
            System.out.println(sentiment); // POSITIVE

            boolean positive = sentimentAnalyzer.isPositive("糟糕!");
            System.out.println(positive); // false
        }
    }


    static class Number_Extracting_AI_Service_Example {

        interface NumberExtractor {

            @UserMessage("提取数字从 {{it}}")
            int extractInt(String text);

            @UserMessage("提取数字从 {{it}}")
            long extractLong(String text);

            @UserMessage("提取数字从 {{it}}")
            BigInteger extractBigInteger(String text);

            @UserMessage("提取数字从 {{it}}")
            float extractFloat(String text);

            @UserMessage("提取数字从 from {{it}}")
            double extractDouble(String text);

            @UserMessage("提取数字从 {{it}}")
            BigDecimal extractBigDecimal(String text);
        }

        public static void main(String[] args) {

            NumberExtractor extractor = AiServices.create(NumberExtractor.class, chatModel);

            String text = "经过无数个千年的运算，超级计算机“深思”最终宣布，关于生命、宇宙以及一切的终极问题的答案是四十二。";

            int intNumber = extractor.extractInt(text);
            System.out.println(intNumber); // 42

            long longNumber = extractor.extractLong(text);
            System.out.println(longNumber); // 42

            BigInteger bigIntegerNumber = extractor.extractBigInteger(text);
            System.out.println(bigIntegerNumber); // 42

            float floatNumber = extractor.extractFloat(text);
            System.out.println(floatNumber); // 42.0

            double doubleNumber = extractor.extractDouble(text);
            System.out.println(doubleNumber); // 42.0

            BigDecimal bigDecimalNumber = extractor.extractBigDecimal(text);
            System.out.println(bigDecimalNumber); // 42.0
        }
    }


    static class Date_and_Time_Extracting_AI_Service_Example {

        interface DateTimeExtractor {

            @UserMessage("提取日期从 {{it}}")
            LocalDate extractDateFrom(String text);

            @UserMessage("提取时间从 {{it}}")
            LocalTime extractTimeFrom(String text);

            @UserMessage("提取日期和时间从 {{it}}")
            LocalDateTime extractDateTimeFrom(String text);
        }

        public static void main(String[] args) {

            DateTimeExtractor extractor = AiServices.create(DateTimeExtractor.class, chatModel);

            String text = "1968年那个傍晚，在独立日庆祝活动结束后，宁静笼罩着整个夜晚，此时距离午夜仅差十五分钟。";

            LocalDate date = extractor.extractDateFrom(text);
            System.out.println(date); // 1968-07-04

            LocalTime time = extractor.extractTimeFrom(text);
            System.out.println(time); // 23:45

            LocalDateTime dateTime = extractor.extractDateTimeFrom(text);
            System.out.println(dateTime); // 1968-07-04T23:45
        }
    }


    static class POJO_Extracting_AI_Service_Example {

        static class Person {

            @Description("这是一个人的信息")
            // you can add an optional description to help an LLM have a better understanding
            private String firstName;
            private String lastName;
            private LocalDate birthDate;

            @Override
            public String toString() {
                return "Person {" +
                        " firstName = \"" + firstName + "\"" +
                        ", lastName = \"" + lastName + "\"" +
                        ", birthDate = " + birthDate +
                        " }";
            }
        }

        interface PersonExtractor {

            @UserMessage("""
            从以下文本中提取人物和出生日期：{{it}}
    
            【硬性规则】
            1. “独立日”代表 7月4日，“圣诞节”代表 12月25日。
            2. 必须推断出完整的 birthDate（包含年、月、日），绝对不能为 null。
            """)
            Person extractPersonFrom(String text);
        }

        public static void main(String[] args) {

            ChatModel chatModel = OpenAiChatModel.builder()
                    .baseUrl("http://localhost:8085/v1")
                    .apiKey("unused")
                    .modelName("Qwen2.5-VL-3B-Custom")
                    // When extracting POJOs with the LLM that supports the "json mode" feature
                    // (e.g., OpenAI, Azure OpenAI, Vertex AI Gemini, Ollama, etc.),
                    // it is advisable to enable it (json mode) to get more reliable results.
                    // When using this feature, LLM will be forced to output a valid JSON.
                    .responseFormat("json_schema")
                    .strictJsonSchema(true) // https://docs.langchain4j.dev/integrations/language-models/open-ai#structured-outputs-for-json-mode
                    .logRequests(true)
                    .logResponses(true)
                    .build();

            PersonExtractor extractor = AiServices.create(PersonExtractor.class, chatModel);

            String text = "1968年，在《独立日》余音渐消之际，一个名叫约翰的孩子在宁静的夜空下呱呱坠地。这个新生儿，姓多伊，标志着一个新旅程的开始。";


            Person person = extractor.extractPersonFrom(text);

            System.out.println(person); // Person { firstName = "John", lastName = "Doe", birthDate = 1968-07-04 }
        }
    }


    static class POJO_With_Descriptions_Extracting_AI_Service_Example {

        static class Recipe {

            @Description("简短标题，最多3个字")
            private String title;

            @Description("简短描述，最多2句话")
            private String description;

            @Description("每一步都应用四个字来描述，步骤之间应押韵")
            private List<String> steps;

            private Integer preparationTimeMinutes;

            @Override
            public String toString() {
                return "食谱 {" +
                        " 标题 = \"" + title + "\"" +
                        ", ，描述 = \"" + description + "\"" +
                        ", 步骤 = " + steps +
                        ", 准备时间（分钟） = " + preparationTimeMinutes +
                        " }";
            }
        }

        @StructuredPrompt("制作一份仅使用{{ingredients}}就能准备的{{dish}}食谱")
        static class CreateRecipePrompt {

            private String dish;
            private List<String> ingredients;
        }

        interface Chef {

            Recipe createRecipeFrom(String... ingredients);

            Recipe createRecipe(CreateRecipePrompt prompt);
        }

        public static void main(String[] args) {

            ChatModel chatModel = OpenAiChatModel.builder()
                    .baseUrl("http://localhost:8085/v1")
                    .apiKey("unused")
                    .modelName("Qwen2.5-VL-3B-Custom")
                    // When extracting POJOs with the LLM that supports the "json mode" feature
                    // (e.g., OpenAI, Azure OpenAI, Vertex AI Gemini, Ollama, etc.),
                    // it is advisable to enable it (json mode) to get more reliable results.
                    // When using this feature, LLM will be forced to output a valid JSON.
                    .responseFormat("json_schema")
                    .strictJsonSchema(true) // https://docs.langchain4j.dev/integrations/language-models/open-ai#structured-outputs-for-json-mode
                    .logRequests(true)
                    .logResponses(true)
                    .build();

            Chef chef = AiServices.create(Chef.class, chatModel);

            Recipe recipe = chef.createRecipeFrom("黄瓜", "番茄", "羊乳酪", "洋葱", "橄榄油");

            System.out.println(recipe);
            // Recipe {
            //     title = "Greek Salad",
            //     description = "A refreshing mix of veggies and feta cheese in a zesty dressing.",
            //     steps = [
            //         "Chop cucumber and tomato",
            //         "Add onion and olives",
            //         "Crumble feta on top",
            //         "Drizzle with dressing and enjoy!"
            //     ],
            //     preparationTimeMinutes = 10
            // }


            CreateRecipePrompt prompt = new CreateRecipePrompt();
            prompt.dish = "沙拉";
            prompt.ingredients = asList("黄瓜", "番茄", "羊乳酪", "洋葱", "橄榄油");

            Recipe anotherRecipe = chef.createRecipe(prompt);
            System.out.println(anotherRecipe);
            // Recipe ...
        }
    }


    static class AI_Service_with_System_Message_Example {

        interface Chef {

            @SystemMessage("You are a professional chef. You are friendly, polite and concise.")
            String answer(String question);
        }

        public static void main(String[] args) {

            Chef chef = AiServices.create(Chef.class, chatModel);

            String answer = chef.answer("How long should I grill chicken?");
            System.out.println(answer); // Grilling chicken usually takes around 10-15 minutes per side, depending on ...
        }
    }


    static class AI_Service_with_System_and_User_Messages_Example {

        interface TextUtils {

            @SystemMessage("You are a professional translator into {{language}}")
            @UserMessage("Translate the following text: {{text}}")
            String translate(@V("text") String text, @V("language") String language);

            @SystemMessage("Summarize every message from user in {{n}} bullet points. Provide only bullet points.")
            List<String> summarize(@UserMessage String text, @V("n") int n);
        }

        public static void main(String[] args) {

            TextUtils utils = AiServices.create(TextUtils.class, chatModel);

            String translation = utils.translate("Hello, how are you?", "italian");
            System.out.println(translation); // Ciao, come stai?


            String text = "AI, or artificial intelligence, is a branch of computer science that aims to create " +
                    "machines that mimic human intelligence. This can range from simple tasks such as recognizing " +
                    "patterns or speech to more complex tasks like making decisions or predictions.";

            List<String> bulletPoints = utils.summarize(text, 3);
            System.out.println(bulletPoints);
            // [
            //     "- AI is a branch of computer science",
            //     "- It aims to create machines that mimic human intelligence",
            //     "- It can perform simple or complex tasks"
            // ]
        }
    }


    static class AI_Service_with_System_and_User_Messages_loaded_from_resources_Example {

        interface TextUtils {

            @SystemMessage(fromResource = "/translator-system-prompt-template.txt")
            @UserMessage(fromResource = "/translator-user-prompt-template.txt")
            String translate(@V("text") String text, @V("language") String language);
        }

        public static void main(String[] args) {

            TextUtils utils = AiServices.create(TextUtils.class, chatModel);

            String translation = utils.translate("Hello, how are you?", "italian");
            System.out.println(translation); // Ciao, come stai?
        }
    }


    static class AI_Service_with_UserName_Example {

        interface Assistant {

            String chat(@UserName String name, @UserMessage String message);
        }

        public static void main(String[] args) {

            Assistant assistant = AiServices.create(Assistant.class, chatModel);

            String answer = assistant.chat("Klaus", "Hi, tell me my name if you see it.");
            System.out.println(answer); // Hello! Your name is Klaus. How can I assist you today?
        }
    }

    static class AI_Service_with_Dynamic_System_Message_Example {

        interface Assistant {

            String chat(@MemoryId String memoryId, @UserMessage String userMessage);
        }

        public static void main(String[] args) {

            Function<Object, String> systemMessageProvider = (memoryId) -> {
                if (memoryId.equals("1")) {
                    return "You are a helpful assistant. The user prefers to be called 'Your Majesty'.";
                } else {
                    return "You are a helpful assistant.";
                }
            };

            Assistant assistant = AiServices.builder(Assistant.class)
                    .chatModel(chatModel)
                    .systemMessageProvider(systemMessageProvider)
                    .build();

            System.out.println(assistant.chat("1", "Hi")); // Hello, Your Majesty! How may I assist you today?
            System.out.println(assistant.chat("2", "Hi")); // Hello! How can I assist you today?
        }
    }
}
