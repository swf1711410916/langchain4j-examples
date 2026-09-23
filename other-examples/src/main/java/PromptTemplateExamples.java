import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.HashMap;
import java.util.Map;

public class PromptTemplateExamples {

    static class PromptTemplate_with_One_Variable_Example {

        public static void main(String[] args) {

            PromptTemplate promptTemplate = PromptTemplate.from("说 '你好' 用 {{it}}.");

            Prompt prompt = promptTemplate.apply("中文");

            System.out.println(prompt.text());
        }
    }

    static class PromptTemplate_With_Multiple_Variables_Example {

        public static void main(String[] args) {

            PromptTemplate promptTemplate = PromptTemplate.from("说 '{{text}}' 用 {{language}}.");

            Map<String, Object> variables = new HashMap<>();
            variables.put("text", "你好");
            variables.put("language", "中文");

            Prompt prompt = promptTemplate.apply(variables);

            System.out.println(prompt.text()); // Say 'hi' in German.
        }
    }
}
