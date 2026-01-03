package andreyz.agent.service.mail;

import andreyz.agent.domain.MailItem;
import andreyz.agent.domain.ParserServiceType;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "mail.gmail.enabled", havingValue = "true")
public class GmailReaderService implements MailReaderService {

    private final Gmail gmail;

    // Ключевые слова в теме, указывающие на письмо со списком вакансий
    private static final Pattern VACANCY_SUBJECT_PATTERN = Pattern.compile(
            "вакансии|vacancies|резюме|new\\s+vacancy|новые\\s+вакансии|подходящие\\s+вакансии",
            Pattern.CASE_INSENSITIVE
    );

    // Отправитель, с которого приходят письма (например, hh.ru)
    private static final String EXPECTED_SENDER_DOMAIN = "hh.ru";

    @PostConstruct
    public void init() {
        log.info("Init Gmail Reader, with gmail: {}", gmail);
        try {
            // Простой запрос: получение основного профиля пользователя
            // Это самый легкий способ проверить, что доступ есть
            Profile profile = gmail.users().getProfile("me").execute();

            log.info("✅ Подключение к Gmail API успешно установлено.");
            log.info("Почта аккаунта: {}", profile.getEmailAddress());
            log.info("Всего писем в ящике: {}", profile.getMessagesTotal());

        } catch (Exception e) {
            log.error("❌ Не удалось подключиться к Gmail API. Проверьте учётные данные и разрешения.", e);
//            throw new IllegalStateException("Не удалось инициализировать GmailReaderService: проверьте OAuth2 настройки, токен доступа и включенный Gmail API в Google Cloud Console.", e);
        }
    }

    @Override
    public List<MailItem> readInbox() throws Exception {
        List<MailItem> results = new ArrayList<>();
        String user = "me";
        String query = "label:vacancy is:unread";

        ListMessagesResponse response = gmail.users().messages()
                .list(user)
                .setMaxResults(10L)
                .setQ(query)
                .execute();

        List<Message> messages = response.getMessages();
        if (messages == null || messages.isEmpty()) {
            log.info("No messages found.");
            return results;
        }

        for (Message m : messages) {
            Message full = gmail.users().messages()
                    .get(user, m.getId())
                    .setFormat("full")
                    .execute();

            if (!isVacancyListEmail(full)) {
                log.info("Письмо ID: {} не является письмом со списком вакансий. Пропускаем.", full.getId());
                continue;
            }

            String subject = getHeader(full, "Subject");
            String body = extractBody(full);
            String id = full.getId(); // Gmail ID — уникальный и пригоден для дальнейших операций

            results.add(new MailItem(id, subject, body, ParserServiceType.GOOGLE));

            log.info("✅ Подтверждено: письмо со списком вакансий. ID: {}, Subject: {}", id, subject);
        }

        log.info("Найдено {} подходящих писем.", results.size());
        return results;
    }

    /**
     * Проверяет, является ли письмо письмом со списком вакансий
     */
    private boolean isVacancyListEmail(Message message) {
        String subject = getHeader(message, "Subject");
        String from = getHeader(message, "From");

        boolean hasRelevantSubject = VACANCY_SUBJECT_PATTERN.matcher(subject).find();
        boolean fromHhRu = from.contains(EXPECTED_SENDER_DOMAIN);

        return hasRelevantSubject && fromHhRu;
    }

    /**
     * Извлекает значение заголовка по имени
     */
    private String getHeader(Message message, String name) {
        return message.getPayload().getHeaders().stream()
                .filter(h -> name.equalsIgnoreCase(h.getName()))
                .map(MessagePartHeader::getValue)
                .findFirst()
                .orElse("");
    }

    /**
     * Извлекает тело письма (поддержка base64 и multipart)
     */
    private String extractBody(Message message) {
        try {
            return extractPlainTextFromPayload(message.getPayload());
        } catch (Exception e) {
            log.warn("Ошибка при извлечении тела письма: {}", e.getMessage());
            return "";
        }
    }

    private String extractPlainTextFromPayload(MessagePart payload) {
        if (payload == null) return "";

        // Если у части есть тело — извлекаем
        if (payload.getBody() != null && payload.getBody().getData() != null) {
            return decodeBase64(payload.getBody().getData());
        }

        // Если есть вложенные части — ищем plain/text
        if (payload.getParts() != null) {
            for (MessagePart part : payload.getParts()) {
                String mimeType = part.getMimeType();
                if ("text/plain".equals(mimeType)) {
                    if (part.getBody() != null && part.getBody().getData() != null) {
                        return decodeBase64(part.getBody().getData());
                    }
                }
            }
            // Рекурсивно ищем дальше
            for (MessagePart part : payload.getParts()) {
                String text = extractPlainTextFromPayload(part);
                if (!text.isEmpty()) return text;
            }
        }

        return "";
    }

    private String decodeBase64(String data) {
        return new String(
                java.util.Base64.getUrlDecoder().decode(data.replaceAll("\\s", "")),
                StandardCharsets.UTF_8
        );
    }

}