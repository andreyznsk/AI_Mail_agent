package andreyz.agent.service;

import andreyz.agent.domain.Vacancy;
import andreyz.agent.service.parsers.HhApiClient;
import andreyz.agent.service.parsers.ParserServiceHeadHunter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserServiceHeadHunterTest {

    private final ParserServiceHeadHunter parserServiceHeadHunter = new ParserServiceHeadHunter(new HhApiClient());


    @Test
    void testParseVacancies_ShouldReturnSixVacancies() throws IOException {
        // When
        List<Vacancy> vacancies = parserServiceHeadHunter.parseVacancies(Files.readString(Paths.get("src/test/resources/test-email.html")));

        // Then
        assertNotNull(vacancies);
        assertEquals(5, vacancies.size(), "Expected 6 vacancies");

        // Проверим первую вакансию
        Vacancy v1 = vacancies.getFirst();
        assertEquals("Senior/Staff Java Engineer в Uzum Market (Старший Разработчик)", v1.title());
        assertEquals("«UZUM TECHNOLOGIES»", v1.company());
        assertNull(v1.salary());
        assertTrue(v1.link().startsWith("https://hh.ru/vacancy/128179669"));
        assertEquals("128179669", v1.vacancyId());
        assertEquals(
                "Чем предстоит заниматься: Придумывать и реализовывать архитектурные решения: вы предлагаете технические решения для реализации целей бизнеса Писать продакшн-код на Java для решения highload-задач и создания стабильных и масштабируемых сервисов Писать тесты, проводить код-ревью Вводить в эксплуатацию свои сервисы вместе с инфраструктурными и платформенными командами Повышать надежность и качество системы на всех уровнях Почему это интересно: Современный стек – Java, Spring Boot, Kotlin, Kafka, PostgreSQL, Redis Большие возможности — нам всего 3 года, но мы уже лидеры рынка в стране. Если вы устали отвечать за очень маленькую часть системы и вам хочется драйва, новых вызовов и роста — все это можно получить у нас Обучение и развитие — мы поддерживаем как внутри компании, так и за ее пределами (митапы, конференции, проф обучение, публикации). А еще помогаем развивать личный бренд Уникальная культура — мы сохранили дух стартапа, при этом уже отстроили зрелые процессы. У нас не корпорация с бюрократией, а коммьюнити людей, которые любят свое дело Формат работы – можно удаленно из любой точки мира или поможем с релокацией в солнечный Ташкенте Что для нас важно: Опыт продуктовой разработки на Java 11+ и Spring Boot 2/3 от 3 лет Понимание работы очередей, опыт работы с Postgres Отличная техническая база, понимание принципов модульной и микросервисной архитектуры Умение взаимодействовать с CI/CD и современным стеком: k8s, Docker, Helm Самое главное: желание решать сложные задачи в команде увлеченных коллег Что мы предлагаем: Конкурентная зарплата — платим на уровне топовых компаний Карьерное развитие — даём действительно неограниченные возможности для роста как в инженерном, так и в менеджерском треке Формат работы — полная удалёнка из любой точки мира или помощь с релокацией в Ташкент Команда — 84% специалистов уровня Senior. Растим комьюнити профессионалов База — бесконечное желание делать круто, а приятный бонус — ДМС в привязке к вашей локации, обучение и другие плюшки"
        ,v1.description());

        // Проверим вакансию с зарплатой
        Vacancy v4 = vacancies.get(3); // 4-я вакансия — с зарплатой
        assertEquals("Java разработчик Middle/Senior (удалённо)", v4.title());
        assertEquals("Riverstart (ООО Риверстарт)", v4.company());
        assertEquals(160000L, v4.salary());
        assertTrue(v4.link().startsWith("https://hh.ru/vacancy/128148220"));
        assertEquals("128148220", v4.vacancyId());

        // Проверим последнюю
        Vacancy v6 = vacancies.get(4);
        assertEquals("Kotlin/ Java разработчик (middle/senior)", v6.title());
        assertEquals("X5 Tech", v6.company());
        assertNull(v6.salary());
        assertTrue(v6.link().startsWith("https://hh.ru/vacancy/127120812"));
        assertEquals("127120812", v6.vacancyId());
    }
}

