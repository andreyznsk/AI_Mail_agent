package sbp.school.performance.service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.FlagTerm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sbp.school.performance.dto.MailItem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class YandexMailService implements MailReaderService {

    private final Session session;

    @Value("${mail.imap.username}")
    private String username;

    @Value("${mail.imap.password}")
    private String password;

    @Value("${mail.imap.inbox-folder}")
    private String inboxFolder;

    // Ключевые слова в теме
    private static final Pattern VACANCY_SUBJECT_PATTERN = Pattern.compile(
            "вакансии|vacancies|резюме|new\\s+vacancy|новые\\s+вакансии|подходящие\\s+вакансии",
            Pattern.CASE_INSENSITIVE
    );

    // Ожидаемый домен отправителя
    private static final String EXPECTED_SENDER_DOMAIN = "hh.ru";


    public YandexMailService(Session session) {
        this.session = session;
    }

    @PostConstruct
    public void init() {
        log.info("Инициализация YandexMailService, проверка подключения к IMAP...");
        Store store = null;
        try {
            store = session.getStore("imap");
            store.connect(username, password);
            Folder inbox = store.getFolder(inboxFolder);
            inbox.open(Folder.READ_ONLY);

            log.info("✅ Подключение к Yandex IMAP успешно установлено.");
            log.info("Папка: {}, Сообщений: {}, Непрочитанных: {}",
                    inbox.getName(),
                    inbox.getMessageCount(),
                    inbox.getUnreadMessageCount());

            inbox.close(false);
        } catch (Exception e) {
            log.error("❌ Не удалось подключиться к Yandex IMAP. Проверьте логин, пароль и настройки доступа к почте.", e);
//            throw new IllegalStateException("Не удалось инициализировать YandexMailService", e);
        } finally {
            if (store != null && store.isConnected()) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.warn("Ошибка при закрытии IMAP соединения", e);
                }
            }
        }
    }

    @Override
    public List<MailItem> readInbox() throws Exception {
        List<MailItem> results = new ArrayList<>();

        Store store = session.getStore("imap");
        store.connect(username, password);

        Folder inbox = store.getFolder(inboxFolder);
        inbox.open(Folder.READ_ONLY);

        try {
            Message[] messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false)
            );

            for (Message message : messages) {
                try {
                    if (isVacancyListEmail(message)) {
                        String subject = message.getSubject();
                        String body = extractBody(message);
                        String id = extractMessageId(message); // получаем уникальный ID

                        results.add(new MailItem(id, subject, body));

                        log.info("✅ Найдено письмо со списком вакансий: ID={}, Subject={}", id, subject);
                    }
                } catch (Exception e) {
                    log.warn("Ошибка при обработке письма: {}", e.getMessage());
                }
            }

            log.info("Найдено {} подходящих писем.", results.size());
            return results;

        } finally {
            inbox.close(false);
            store.close();
        }
    }

    private String extractMessageId(Message message) throws MessagingException {
        String[] ids = message.getHeader("Message-ID");
        if (ids != null && ids.length > 0) {
            return ids[0];
        }
        // Резерв: использовать индекс IMAP, но он нестабилен
        return "IMAP-" + message.getMessageNumber();
    }

    private boolean isVacancyListEmail(Message message) throws MessagingException {
        String subject = message.getSubject();
        String from = message.getFrom()[0].toString();

        boolean hasRelevantSubject = subject != null && VACANCY_SUBJECT_PATTERN.matcher(subject).find();
        boolean fromHhRu = from != null && from.contains(EXPECTED_SENDER_DOMAIN);

        return hasRelevantSubject && fromHhRu;
    }

    private String extractBody(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/plain")) {
            return decodeText(part.getContent().toString());
        } else if (part.isMimeType("text/html")) {
            return decodeText(part.getContent().toString()); // можно обработать HTML, но пока как строку
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    return extractBody(bodyPart);
                }
            }
            // Если нет plain text — попробуем HTML
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/html")) {
                    return extractBody(bodyPart);
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            return extractBody((Part) part.getContent());
        }

        return "";
    }

    private String decodeText(String text) {
        try {
            return MimeUtility.decodeText(text);
        } catch (Exception e) {
            return new String(text.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }
    }
}
