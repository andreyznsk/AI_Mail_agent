package andreyz.agent.service.draftGenerator;

import andreyz.agent.domain.draftAnswer.DraftEmailRequest;
import andreyz.agent.domain.draftAnswer.VacancyCoverLetter;
import andreyz.agent.service.mail.MailReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static andreyz.agent.domain.draftAnswer.DraftHtmlGenerator.buildVacancyTableHtml;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftEmailServiceMail implements DraftEmailService {
    private final MailReaderService emailClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void createDraftWithVacancies(String originalMessageId, String to, ZonedDateTime originalMessageDate, List<VacancyCoverLetter> vacancies) {
        // 1. Пометить исходное письмо как прочитанное
        emailClient.markAsRead(originalMessageId);

        // 2. Построить HTML таблицу
        String htmlBody = buildVacancyTableHtml(vacancies);

        // 3. Создать тему письма с датой

        String subject = "CR на вакансии от " + originalMessageDate.format(formatter);


        // 3. Создать черновик письма
        DraftEmailRequest request = new DraftEmailRequest(to, subject, htmlBody);

        emailClient.createDraftEmail(request);
    }
}
