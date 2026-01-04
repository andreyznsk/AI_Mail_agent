package andreyz.agent.service.CLGenerator;

import andreyz.agent.domain.CoverLetter.CoverLetterRequest;
import andreyz.agent.domain.CoverLetter.CoverLetterResponse;

public interface CoverLetterGenerator {
    CoverLetterResponse generate(CoverLetterRequest request);
}
