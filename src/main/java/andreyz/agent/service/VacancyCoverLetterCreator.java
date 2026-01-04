package andreyz.agent.service;

import andreyz.agent.domain.CoverLetter.CoverLetterRequest;
import andreyz.agent.domain.CoverLetter.CoverLetterResponse;
import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.resume.Resume;
import andreyz.agent.domain.resumeMatcher.MatchResult;
import andreyz.agent.dto.MailItem;
import andreyz.agent.service.CLGenerator.CoverLetterGenerator;
import andreyz.agent.service.mail.MailReaderService;
import andreyz.agent.service.parsers.ParserService;
import andreyz.agent.service.resume.ResumeParsingService;
import andreyz.agent.service.resume.ResumeSource;
import andreyz.agent.service.resumeMatcher.ResumeVacancyMatcher;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@EnableScheduling
public class VacancyCoverLetterCreator {

    private final List<MailReaderService> mailReaderServices;
    private final ResumeParsingService cachedResumeParsingService;
    private final ResumeSource resumeSource;
    private final CoverLetterGenerator coverLetterGenerator;
    private final ParserService hhParseService;
    private final ResumeVacancyMatcher resumeVacancyMatcher;

    private Resume currentResume;

    public VacancyCoverLetterCreator(
            List<MailReaderService> mailReaderServices,
            @Qualifier("cachedResumeParsingService") ResumeParsingService cachedResumeParsingService,
            ResumeSource resumeSource,
            CoverLetterGenerator coverLetterGenerator,
            ParserService hhParseService,
            ResumeVacancyMatcher resumeVacancyMatcher) {
        this.mailReaderServices = mailReaderServices;
        this.cachedResumeParsingService = cachedResumeParsingService;
        this.resumeSource = resumeSource;
        this.coverLetterGenerator = coverLetterGenerator;
        this.hhParseService = hhParseService;
        this.resumeVacancyMatcher = resumeVacancyMatcher;
    }

    @PostConstruct
    public void getStartedResume(){

        currentResume = cachedResumeParsingService.parse(resumeSource.load());

        log.info("started with resume {}", currentResume);
    }


    @Scheduled(fixedRate = 60000)
    public void createVacanciesCoverLetter() throws Exception {


        List<MailItem> mailItems = new LinkedList<>();

        for (MailReaderService mailReaderService : mailReaderServices) {
            mailItems.addAll(mailReaderService.readInbox());
        }

        for (MailItem mailItem : mailItems) {
            switch (mailItem.parserServiceType()){
                case  GOOGLE -> log.info("GOG");
                case  YANDEX -> {
                    List<Vacancy> vacancies = hhParseService.parseVacancies(mailItem.body());
                    for (Vacancy vacancy : vacancies) {
                        MatchResult match = resumeVacancyMatcher.match(currentResume, vacancy);
                        CoverLetterResponse coverLetter = coverLetterGenerator.generate(new CoverLetterRequest(currentResume, vacancy, match, Locale.forLanguageTag("ru-RU")));
                        log.info("Cover letter for vacancy title, company: {}, {} is: {}", vacancy.title(), vacancy.company(), coverLetter);
                    }
                }
            }

        }

    }
}
