package andreyz.agent.service.draftGenerator;

import andreyz.agent.domain.draftAnswer.VacancyCoverLetter;

import java.time.ZonedDateTime;
import java.util.List;

public interface DraftEmailService {
    void createDraftWithVacancies(String originalMessageId, String to, ZonedDateTime originalMessageDate, List<VacancyCoverLetter> vacancies);
}
