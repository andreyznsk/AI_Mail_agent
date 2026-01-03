package andreyz.agent.service.llm;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LlmClientImpl implements LlmClient {

    private final ChatClient chatClient;

    @PostConstruct
    public void modelTest(){
        log.info("model Type:  {}", chatClient.prompt(PROMPT).call().content());
    }

    @Override
    public String complete(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }
}
