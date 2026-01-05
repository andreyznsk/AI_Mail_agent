package andreyz.agent.persistence.dto;

import andreyz.agent.domain.CoverLetter.CoverLetterResponse;
import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resumeMatcher.MatchResult;
import andreyz.agent.dto.ParserServiceType;

public record VacancyProcessingResult(
        Vacancy vacancy,
        MatchResult matchResult,
        CoverLetterResponse coverLetter,
        ParserServiceType parserServiceType) {}
