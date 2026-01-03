package andreyz.agent.service;

import andreyz.agent.domain.resume.Resume;
import andreyz.agent.dto.MailItem;
import andreyz.agent.service.mail.MailReaderService;
import andreyz.agent.service.resume.ResumeParsingService;
import andreyz.agent.service.resume.ResumeSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@EnableScheduling
public class VacancyCoverLetterCreator {

    private final List<MailReaderService> mailReaderServices;
    private final ResumeParsingService cachedResumeParsingService;
    private final ResumeSource resumeSource;

    private Resume currentResume;

    public VacancyCoverLetterCreator(
            List<MailReaderService> mailReaderServices,
            @Qualifier("cachedResumeParsingService") ResumeParsingService cachedResumeParsingService,
            ResumeSource resumeSource) {
        this.mailReaderServices = mailReaderServices;
        this.cachedResumeParsingService = cachedResumeParsingService;
        this.resumeSource = resumeSource;
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
                case  YANDEX -> log.info("ya items size: {}", mailItems.size());
            }

        }

    }
}
