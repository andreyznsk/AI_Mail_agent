package andreyz.agent.service;

import andreyz.agent.dto.MailItem;
import andreyz.agent.service.mail.MailReaderService;
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

     @Scheduled(fixedRate = 60000)
    public void createVacanciesCoverLetter() throws Exception {


        List<MailItem> mailItems = new LinkedList<>();

        for (MailReaderService mailReaderService : mailReaderServices) {
            mailItems.addAll(mailReaderService.readInbox());
        }

        for (MailItem mailItem : mailItems) {
            switch (mailItem.parserServiceType()){
                case  GOOGLE -> log.info(mailItem.toString());
                case  YANDEX -> log.info(mailItem.toString());
            }

        }

    }

}
