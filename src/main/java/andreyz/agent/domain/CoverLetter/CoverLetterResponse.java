package andreyz.agent.domain.CoverLetter;

import java.util.List;
import java.util.Map;


public record CoverLetterResponse(
        String text,
        Map<String, String> sections, // intro, skills, closing
        List<String> warnings
) {}
