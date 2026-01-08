package andreyz.agent.service.resumeMatcher;

import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resume.Resume;
import andreyz.agent.domain.resumeMatcher.MatchResult;

import java.util.Optional;

public interface ResumeVacancyMatcher {
    Optional<MatchResult> match(Resume resume, Vacancy vacancy);
}
