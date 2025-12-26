package andreyz.agent.service.mail;

import andreyz.agent.dto.MailItem;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
@ConditionalOnProperty(name = "mail.mock.enabled", havingValue = "true")
public class MailReaderServiceMock implements MailReaderService {

    @PostConstruct
    void init() {
        log.warn("Внимание!! Запущен мок сервис!");
    }

    @Override
    public List<MailItem> readInbox() {
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File("mail_items/mailItem.seri");
//
        try (InputStream inputStream = new FileInputStream("mail_item/mailItem.seri")) {
            Object deserialize = SerializationUtils.deserialize(inputStream.readAllBytes());
            if (deserialize instanceof MailItem item) {
                log.info("✅ Успешно десериализован мок-объект: {}", item);
                return List.of(item);
            } else throw new IllegalStateException("Some problem with mock");
        } catch (IOException e) {
            log.error("❌ Ошибка при чтении файла из classpath: mailitems/mailItem.seri", e);
            return List.of();

        }
    }
}
