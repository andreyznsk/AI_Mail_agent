package andreyz.agent.service;

import andreyz.agent.domain.CoverLetter.CoverLetterRequest;
import andreyz.agent.domain.CoverLetter.CoverLetterResponse;
import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.draftAnswer.VacancyCoverLetter;
import andreyz.agent.domain.resume.Resume;
import andreyz.agent.dto.MailItem;
import andreyz.agent.persistence.dto.VacancyProcessingResult;
import andreyz.agent.persistence.entity.VacancySnapshotEntity;
import andreyz.agent.persistence.repo.CoverLetterSnapshotRepository;
import andreyz.agent.persistence.repo.VacancySnapshotRepository;
import andreyz.agent.service.CLGenerator.CoverLetterGenerator;
import andreyz.agent.service.draftGenerator.DraftEmailService;
import andreyz.agent.service.mail.MailReaderService;
import andreyz.agent.service.parsers.ParserService;
import andreyz.agent.service.persist.VacancyPersistService;
import andreyz.agent.service.resume.ResumeParsingService;
import andreyz.agent.service.resume.ResumeSource;
import andreyz.agent.service.resumeMatcher.ResumeVacancyMatcher;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@EnableScheduling
public class VacancyCoverLetterCreator {

    @Value("${mail.to:test@test.ru}")
    private String to;

    private final List<MailReaderService> mailReaderServices;
    private final ResumeParsingService cachedResumeParsingService;
    private final ResumeSource resumeSource;
    private final CoverLetterGenerator coverLetterGenerator;
    private final ParserService hhParseService;
    private final ResumeVacancyMatcher resumeVacancyMatcher;
    private final VacancyPersistService vacancyPersistService;
    private final VacancySnapshotRepository vacancyRepository;
    private final CoverLetterSnapshotRepository coverLetterSnapshotRepository;
    private final DraftEmailService draftEmailService;


    private Resume currentResume;

    public VacancyCoverLetterCreator(
            List<MailReaderService> mailReaderServices,
            @Qualifier("cachedResumeParsingService") ResumeParsingService cachedResumeParsingService,
            ResumeSource resumeSource,
            CoverLetterGenerator coverLetterGenerator,
            ParserService hhParseService, ResumeVacancyMatcher resumeVacancyMatcher,
            VacancyPersistService vacancyPersistService, VacancySnapshotRepository vacancyRepository, CoverLetterSnapshotRepository coverLetterSnapshotRepository, DraftEmailService draftEmailService) {
        this.mailReaderServices = mailReaderServices;
        this.cachedResumeParsingService = cachedResumeParsingService;
        this.resumeSource = resumeSource;
        this.coverLetterGenerator = coverLetterGenerator;
        this.hhParseService = hhParseService;
        this.resumeVacancyMatcher = resumeVacancyMatcher;
        this.vacancyPersistService = vacancyPersistService;
        this.vacancyRepository = vacancyRepository;
        this.coverLetterSnapshotRepository = coverLetterSnapshotRepository;
        this.draftEmailService = draftEmailService;
    }

    @PostConstruct
    public void getStartedResume() {

        currentResume = cachedResumeParsingService.parse(resumeSource.load());

        log.info("started with resume {}", currentResume);
    }


    @Scheduled(fixedRate = 60000)
    public void createVacanciesCoverLetter() throws Exception {
        log.info("/* ======================= START CR CREATOR ======================= */");

        List<MailItem> mailItems = new LinkedList<>();

        for (MailReaderService mailReaderService : mailReaderServices) {
            mailItems.addAll(mailReaderService.readInbox());
        }

        for (MailItem mailItem : mailItems) {
            switch (mailItem.parserServiceType()) {
                case GOOGLE -> log.info("GOG");
                case YANDEX -> {
                    List<Vacancy> vacancies = hhParseService.parseVacancies(mailItem.body());
                    List<VacancyCoverLetter> result = new ArrayList<>();

                    for (Vacancy vacancy : vacancies) {

                        Optional<VacancySnapshotEntity> existingVacancy =
                                vacancyRepository.findBySourceIdAndSource(vacancy.vacancyId(), vacancy.source().getName());

                        if (existingVacancy.isPresent()) {
                            // Вакансия уже есть — берем существующий CL
                            coverLetterSnapshotRepository
                                    .findFirstByVacancyId(existingVacancy.get().getId())
                                    .ifPresent(cl -> result.add(new VacancyCoverLetter(vacancy.title(), vacancy.link(), cl.getCoverLetterText())));

                            log.info("Vacancy {} from {} already processed, adding existing CR",
                                    vacancy.vacancyId(), vacancy.source().getName());
                            continue; // пропускаем генерацию нового CL
                        }

                        // Вакансия новая — генерируем CL
                        resumeVacancyMatcher.match(currentResume, vacancy).ifPresent(match -> {
                            CoverLetterResponse coverLetter = coverLetterGenerator.generate(
                                    new CoverLetterRequest(currentResume, vacancy, match, Locale.forLanguageTag("ru-RU"))
                            );

                            // Сохраняем результат в БД
                            vacancyPersistService.persist(new VacancyProcessingResult(vacancy, match, coverLetter, mailItem.parserServiceType()));

                            // Добавляем в список для черновика
                            result.add(new VacancyCoverLetter(vacancy.title(), vacancy.link(), coverLetter.text()));

                            log.debug("Generated new Cover Letter for vacancy {} ({})", vacancy.title(), vacancy.company());
                        });

                        log.info("Vacancy {} processing finished", vacancy.vacancyId());
                    }

                    // Создаем черновик письма
                    draftEmailService.createDraftWithVacancies(mailItem.id(), to, mailItem.originalDateTime(), result);
                }
            }

        }
        log.info("/* ======================= FINISHED CR CREATOR ======================= */");

    }
}
