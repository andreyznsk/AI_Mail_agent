package andreyz.agent.service.llm;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LlmClientImpl implements LlmClient {

    private final ChatClient chatClient;

    @PostConstruct
    public void modelTest() {
        String promptText = "Ответь кратко: какая ты модель и кто твой производитель?";

        try {
            // 1. Запрос к модели — кто она
            String identityResponse = chatClient
                    .prompt(promptText)
                    .call()
                    .content();

            // 2. Получаем метаданные ответа (может включать имя модели)
            ChatResponseMetadata metadata = chatClient
                    .prompt(promptText)
                    .call()
                    .chatResponse().getMetadata();

            log.info("📋 Тест модели:");
            log.info("  • Ответ модели: '{}'", identityResponse.trim());
            log.info("  • Модель из метаданных: '{}'", metadata.getModel());

        } catch (Exception e) {
            log.error("❌ Не удалось проверить модель", e);
            throw new RuntimeException("LLM not available", e);
        }
    }

    @Override
    public String complete(String prompt) {
        return chatClient.prompt(prompt).call().content().replaceAll("`", "").replaceAll("^(?:json)?", "").trim();
    }
}
