package andreyz.agent.service.mail;

import andreyz.agent.domain.draftAnswer.DraftEmailRequest;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static andreyz.agent.utils.MailDateUtils.toZonedDateTime;

@Service
@Slf4j
@ConditionalOnProperty(name = "mail.mock.enabled", havingValue = "true")
public class MailReaderServiceMock implements MailReaderService {

    private final List<MailItem> inbox = new ArrayList<>();
    private final List<DraftEmailRequest> drafts = new ArrayList<>();


    @PostConstruct
    void init() {
        log.warn("Внимание!! Запущен мок сервис!");
        String mockFile = "test/test-email.html";
        try (InputStream inputStream = new FileInputStream(mockFile)) {
            inbox.add(new MailItem("testId_1", "Test Subj", new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), ParserServiceType.YANDEX, toZonedDateTime(new Date())));
        } catch (IOException e) {
            log.error("❌ Ошибка при чтении файла из classpath: mailitems/mailItem.seri", e);
            throw new RuntimeException(e);

        }
    }

    @Override
    public List<MailItem> readInbox() {
        return new ArrayList<>(inbox);
    }


    @Override
    public void markAsRead(String originalMessageId) {
        log.info("Mock: помечаем письмо {} как прочитанное", originalMessageId);
        inbox.removeIf(m -> m.id().equals(originalMessageId));
    }

    @Override
    public void createDraftEmail(DraftEmailRequest request) {
        log.info("Mock: создаем черновик для {}, subject={}", request.to(), request.subject());
        log.debug(request.htmlBody());
        drafts.add(request);
    }
}
