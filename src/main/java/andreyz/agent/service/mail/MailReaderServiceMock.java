package andreyz.agent.service.mail;

import andreyz.agent.dto.MailItem;
import andreyz.agent.dto.ParserServiceType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

        String mockFile = "test/test-email.html";
        try (InputStream inputStream = new FileInputStream(mockFile)) {
            return List.of(new MailItem("testId_1", "Test Subj", new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), ParserServiceType.YANDEX));
        } catch (IOException e) {
            log.error("❌ Ошибка при чтении файла из classpath: mailitems/mailItem.seri", e);
            return List.of();

        }
    }
}
