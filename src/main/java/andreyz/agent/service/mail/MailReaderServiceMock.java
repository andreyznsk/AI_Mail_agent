package andreyz.agent.service.mail;

import andreyz.agent.domain.MailItem;
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

    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    @Override
    public List<MailItem> readInbox() {
//
        String mockFile = "test/mailitems.ser";
        try (InputStream inputStream = new FileInputStream(mockFile)) {
            Object deserialize = SerializationUtils.deserialize(inputStream.readAllBytes());
            if (deserialize instanceof List item) {
                log.info("✅ Успешно десериализован мок-объект: {}", mockFile);
                return item;
            } else throw new IllegalStateException("Some problem with mock");
        } catch (IOException e) {
            log.error("❌ Ошибка при чтении файла из classpath: mailitems/mailItem.seri", e);
            return List.of();

        }
    }
}
