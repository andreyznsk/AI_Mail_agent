package andreyz.agent.domain.CoverLetter;


import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resume.Resume;
import andreyz.agent.domain.resumeMatcher.MatchResult;
import andreyz.agent.service.CLGenerator.CoverLetterLanguage;

import java.util.Locale;

public record CoverLetterRequest(
        Resume resume,
        Vacancy vacancy,
        MatchResult matchResult,
        Locale locale
) {

    public String language() {
        return CoverLetterLanguage.fromLocale(locale).promptName();
    }
}