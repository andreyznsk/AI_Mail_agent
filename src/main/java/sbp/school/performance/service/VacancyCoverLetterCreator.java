package sbp.school.performance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sbp.school.performance.dto.MailItem;
import sbp.school.performance.service.mail.MailReaderService;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VacancyCoverLetterCreator {

    private final List<MailReaderService> mailReaderServices;

    public void createVacanciesCL() throws Exception {


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
