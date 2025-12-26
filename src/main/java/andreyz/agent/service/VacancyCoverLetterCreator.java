package andreyz.agent.service;

import andreyz.agent.dto.MailItem;
import andreyz.agent.service.mail.MailReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@EnableScheduling
public class VacancyCoverLetterCreator {

    private final List<MailReaderService> mailReaderServices;
    private static final String SERIALIZED_FILE_PATH = "mailitems.ser";

     @Scheduled(fixedRate = 60000)
    public void createVacanciesCoverLetter() throws Exception {


        List<MailItem> mailItems = new LinkedList<>();

        for (MailReaderService mailReaderService : mailReaderServices) {
            mailItems.addAll(mailReaderService.readInbox());
        }

//         serializeMailItems(mailItems); for test only!

        for (MailItem mailItem : mailItems) {
            switch (mailItem.parserServiceType()){
                case  GOOGLE -> log.info(mailItem.toString());
                case  YANDEX -> log.info(mailItem.toString());
            }

        }

    }

    private void serializeMailItems(List<MailItem> mailItems) {
    try {
        byte[] serializedBytes = SerializationUtils.serialize(mailItems);
        Path path = Paths.get(SERIALIZED_FILE_PATH);
        Files.write(path, serializedBytes);
        log.info("✅ Successfully serialized {} MailItem(s) to: {}", mailItems.size(), path.toAbsolutePath());
    } catch (Exception e) {
        log.error("❌ Failed to serialize mailItems", e);
    }
}

}
