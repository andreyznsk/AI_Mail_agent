package sbp.school.performance.config;

import jakarta.mail.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class YandexMailConfig {

    @Bean
    public Session yandexMailSession() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.host", "imap.yandex.com");
        props.put("mail.imap.port", "993");

        return Session.getDefaultInstance(props);
    }
}
