package andreyz.agent.service.CLGenerator;


import andreyz.agent.domain.CoverLetter.CoverLetterRequest;
import andreyz.agent.domain.CoverLetter.CoverLetterResponse;
import andreyz.agent.service.llm.LlmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlMCoverLetterGenerator implements CoverLetterGenerator {

    private final LlmClient llmClient;


    @Override
    public CoverLetterResponse generate(CoverLetterRequest request) {
        String prompt = CoverLetterPromptBuilder.build(request);  // детерминированный prompt
        String llmOutput = llmClient.complete(prompt);

        return CoverLetterValidator.parseAndValidate(llmOutput, request);
    }
}
