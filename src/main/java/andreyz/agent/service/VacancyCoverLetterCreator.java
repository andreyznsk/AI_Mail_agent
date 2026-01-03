package andreyz.agent.service;

import andreyz.agent.domain.resume.Resume;
import andreyz.agent.dto.MailItem;
import andreyz.agent.service.mail.MailReaderService;
import andreyz.agent.service.resume.LlmResumeParsingService;
import andreyz.agent.service.resume.ResumeSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@EnableScheduling
public class VacancyCoverLetterCreator {

    private final List<MailReaderService> mailReaderServices;
    private final LlmResumeParsingService resumeParsingService;
    private final ResumeSource resumeSource;

    private Resume currentResume;



    @PostConstruct
    public void getStartedResume(){
        currentResume = resumeParsingService.parse(resumeSource.load());
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
