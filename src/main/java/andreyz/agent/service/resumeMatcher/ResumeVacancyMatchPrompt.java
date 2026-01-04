package andreyz.agent.service.resumeMatcher;

import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resume.Resume;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResumeVacancyMatchPrompt {

    public static String build(Resume resume, Vacancy vacancy) {
        return """
        You are an expert technical recruiter.

        Analyze the resume and the vacancy.
        Return ONLY valid JSON.
        No explanations.

        JSON schema:
        {
          "score": number (0-100),
          "strengths": ["string"],
          "gaps": ["string"],
          "summary": "string"
        }

        Resume:
        %s

        Vacancy:
        %s
        """.formatted(
            toJson(resume),
            toJson(vacancy)
        );
    }

    private static String toJson(Object o) {
        try {
            return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
