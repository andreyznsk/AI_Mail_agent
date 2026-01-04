package andreyz.agent.service.resume;

public class ResumeParsingPrompt {

    public static String build(String rawResumeText) {
        return """
        You are a resume parser.

        Extract structured data from the resume text below.
        Return ONLY valid JSON.
        Do NOT add explanations.
        
        yearsOfExperience usually like: Опыт работы — 19 лет
        
        skills.level only enum: BASIC | INTERMEDIATE | STRONG - If you cannot match it, set BASIC.
        
        JSON schema:
        {
          "personalInfo": {
            "fullName": "string",
            "currentTitle": "string",
            "yearsOfExperience": number,
            "location": "string"
          },
          "experience": [
            {
              "company": "string",
              "role": "string",
              "period": "string",
              "responsibilities": ["string"],
              "technologies": ["string"]
            }
          ],
          "skills": [
            {
              "name": "string",
              "level": "BASIC | INTERMEDIATE | STRONG"
            }
          ],
          "education": [
            {
              "institution": "string",
              "degree": "string",
              "period": "string"
            }
          ]
        }

        Resume text:
        ---
        %s
        ---
        """.formatted(rawResumeText);
    }
}
