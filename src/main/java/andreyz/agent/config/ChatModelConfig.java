package andreyz.agent.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ChatModelConfig {


    @Bean
    @ConditionalOnProperty(name = "spring.ai.model.chat", havingValue = "ollama")
    public ChatClient chatClientOllama(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder().order(4).build())
                .defaultOptions(OllamaOptions.builder()
                        .temperature(0.0)
                        .topP(1.0)
                        .topK(20)
                        .repeatPenalty(1.0)
                        .build())
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.ai.model.chat", havingValue = "gigachat")
    public ChatClient chatClientGiga(ChatClient.Builder builder) {
        return builder.defaultAdvisors(SimpleLoggerAdvisor.builder().order(4).build()).build();
    }

}
