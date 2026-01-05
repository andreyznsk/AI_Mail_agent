package andreyz.agent.service.resume;

import andreyz.agent.domain.resume.Resume;
import andreyz.agent.exception.ResumeParsingException;
import andreyz.agent.service.llm.LlmClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LlmResumeParsingService implements ResumeParsingService {

    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    @Override
    public Resume parse(String rawResumeText) {
        String prompt = ResumeParsingPrompt.build(rawResumeText);
        String json = llmClient.complete(prompt);
        try {
            return objectMapper.readValue(json, Resume.class);
        } catch (Exception e) {
            throw new ResumeParsingException("Failed to parse resume JSON", e);
        }
    }
}
