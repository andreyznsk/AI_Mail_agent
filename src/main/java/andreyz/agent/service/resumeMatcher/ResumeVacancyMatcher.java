package andreyz.agent.service.resumeMatcher;

import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resume.Resume;
import andreyz.agent.domain.resumeMatcher.MatchResult;

public interface ResumeVacancyMatcher {
    MatchResult match(Resume resume, Vacancy vacancy);
}
