package andreyz.agent.service.resumeMatcher;

import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resume.Resume;
import andreyz.agent.domain.resumeMatcher.MatchResult;
import andreyz.agent.service.llm.LlmClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LlmResumeVacancyMatcher implements ResumeVacancyMatcher {

    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    @Override
    public MatchResult match(Resume resume, Vacancy vacancy) {
        String prompt = ResumeVacancyMatchPrompt.build(resume, vacancy);
        String json = llmClient.complete(prompt)
                .replaceAll("`", "")
                .replaceAll("^(?:json)?", "")
                .trim();

        try {
            return MatchResultValidator.validate(objectMapper.readValue(json, MatchResult.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse match result", e);
        }
    }
}
