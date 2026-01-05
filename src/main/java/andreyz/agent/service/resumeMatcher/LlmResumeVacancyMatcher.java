package andreyz.agent.service.resumeMatcher;

import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resume.Resume;
import andreyz.agent.domain.resumeMatcher.MatchResult;
import andreyz.agent.service.llm.LlmClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmResumeVacancyMatcher implements ResumeVacancyMatcher {

    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<MatchResult> match(Resume resume, Vacancy vacancy) {
        String prompt = ResumeVacancyMatchPrompt.build(resume, vacancy);
        String json = llmClient.complete(prompt);

        try {
            return Optional.of(MatchResultValidator.validate(objectMapper.readValue(json, MatchResult.class)));
        } catch (Exception e) {
           log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
