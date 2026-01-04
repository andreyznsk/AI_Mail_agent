package andreyz.agent.service.CLGenerator;

import andreyz.agent.domain.CoverLetter.CoverLetterRequest;
import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resume.Experience;
import andreyz.agent.domain.resume.Resume;
import andreyz.agent.domain.resume.Skill;
import andreyz.agent.domain.resumeMatcher.MatchResult;

import java.util.List;
import java.util.stream.Collectors;

public class CoverLetterPromptBuilder {

    public static String build(CoverLetterRequest request) {
        Resume r = request.resume();
        Vacancy v = request.vacancy();
        MatchResult m = request.matchResult();

        return """
        You are a professional technical recruiter and cover letter writer.

        TASK:
        Write a highly targeted cover letter that explains
        WHY the candidate is a strong match for THIS specific vacancy.
        
        Language:
        Write the cover letter strictly in %s.

        OUTPUT FORMAT:
        Strict JSON with fields:
        - intro
        - skills
        - closing

        ABSOLUTE RULES:
        - Use ONLY information from Resume and MatchResult
        - DO NOT invent experience, skills, or achievements
        - Emphasize MatchResult.strengths
        - DO NOT mention MatchResult.gaps explicitly
        - Base reasoning on Vacancy description
        - Do NOT output anything except JSON

        =====================
        VACANCY CONTEXT
        =====================
        Company: %s
        Role: %s

        Vacancy description:
        %s

        =====================
        CANDIDATE PROFILE
        =====================
        Full name: %s
        Current title: %s
        Years of experience: %d
        Location: %s

        =====================
        RELEVANT EXPERIENCE
        =====================
        %s

        =====================
        SKILLS (from Resume)
        =====================
        %s

        =====================
        MATCH RESULT (ANALYSIS)
        =====================
        Match score: %d

        Strengths (must be reflected in the letter):
        %s

        Gaps (do NOT mention directly):
        %s

        Summary (defines overall tone and positioning):
        %s

        =====================
        WRITING GUIDELINES
        =====================
        - Intro: briefly position the candidate for this role
        - Skills: explain how candidate experience matches vacancy responsibilities
        - Closing: motivation, interest in company culture and challenges

        Remember:
        This is NOT a generic cover letter.
        This is a response to THIS vacancy.
        """
        .formatted(
                request.language(),
            v.company(),
            v.title(),
            v.description(),

            r.getPersonalInfo().fullName(),
            r.getPersonalInfo().currentTitle(),
            r.getPersonalInfo().yearsOfExperience() / 12,
            r.getPersonalInfo().location(),

            formatExperience(r.getExperience()),
            formatSkills(r.getSkills()),

            m.score(),
            formatList(m.strengths()),
            formatList(m.gaps()),
            m.summary()
        );
    }

    private static String formatExperience(List<Experience> experience) {
        return experience.stream()
            .map(e -> """
                Company: %s
                Role: %s
                Responsibilities:
                %s
                Technologies:
                %s
                """
                .formatted(
                    e.company(),
                    e.role(),
                    formatList(e.responsibilities()),
                    formatList(e.technologies())
                )
            ).collect(Collectors.joining("\n"));
    }

    private static String formatSkills(List<Skill> skills) {
        return skills.stream()
            .map(s -> "- %s (%s)".formatted(s.name(), s.level()))
            .collect(Collectors.joining("\n"));
    }

    private static String formatList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "- none";
        }
        return items.stream()
            .map(i -> "- " + i)
            .collect(Collectors.joining("\n"));
    }
}
