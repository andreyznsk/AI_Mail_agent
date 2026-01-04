package andreyz.agent.service.CLGenerator;

import andreyz.agent.domain.CoverLetter.CoverLetterRequest;
import andreyz.agent.domain.CoverLetter.CoverLetterResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoverLetterValidator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static CoverLetterResponse parseAndValidate(String llmOutput, CoverLetterRequest request) {
        List<String> warnings = new ArrayList<>();
        Map<String, String> sections = new HashMap<>();
        String fullText;

        try {
            JsonNode root = MAPPER.readTree(llmOutput);

            for (String field : List.of("intro", "skills", "closing")) {
                if (root.has(field)) {
                    String text = root.get(field).asText();
                    sections.put(field, text);
                } else {
                    warnings.add("Missing field in LLM output: " + field);
                    sections.put(field, ""); // fallback empty
                }
            }
            fullText = String.join("\n\n", sections.get("intro"), sections.get("skills"), sections.get("closing"));
            String normalize = fullText.toLowerCase();

            // skills section must reference strengths
            for (String strength : request.matchResult().strengths()) {
                if (!normalize.contains(strength.toLowerCase())) {
                    warnings.add("Strength from MatchResult not reflected in CL: " + strength);
                }
            }

            for (String gap : request.matchResult().gaps()) {
                if (normalize.contains(gap.toLowerCase())) {
                    warnings.add("Gap mentioned in Cover Letter: " + gap);
                }
            }

        } catch (Exception e) {
            warnings.add("LLM output parsing failed: " + e.getMessage());
            fullText = "Dear Hiring Manager,\n\nI am interested in this position.\n\nSincerely,\n" + request.resume().getPersonalInfo().fullName();
            sections.put("intro", "");
            sections.put("skills", "");
            sections.put("closing", "");
        }

        return new CoverLetterResponse(fullText, sections, warnings);
    }
}
