package sbp.school.performance.service;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import sbp.school.performance.dto.Vacancy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ParserServiceHeadHunterTest {

    private final ParserServiceHeadHunter parserServiceHeadHunter = new ParserServiceHeadHunter();



    @Test
    void testParseVacancies_ShouldReturnSixVacancies() throws IOException {
        // When
        List<Vacancy> vacancies = parserServiceHeadHunter.parseVacancies(Files.readString(Paths.get("src/test/resources/test-email.html")));

        // Then
        assertNotNull(vacancies);
        assertEquals(6, vacancies.size(), "Expected 6 vacancies");

        // Проверим первую вакансию
        Vacancy v1 = vacancies.getFirst();
        assertEquals("Senior/Staff Java Engineer в Uzum Market (Старший Разработчик)", v1.title());
        assertEquals("«UZUM TECHNOLOGIES»", v1.company());
        assertNull(v1.salary());
        assertTrue(v1.link().startsWith("https://hh.ru/vacancy/128179669"));

        // Проверим вакансию с зарплатой
        Vacancy v4 = vacancies.get(3); // 4-я вакансия — с зарплатой
        assertEquals("Java разработчик Middle/Senior (удалённо)", v4.title());
        assertEquals("Riverstart (ООО Риверстарт)", v4.company());
        assertEquals(160000L, v4.salary());
        assertTrue(v4.link().startsWith("https://hh.ru/vacancy/128148220"));

        // Проверим последнюю
        Vacancy v6 = vacancies.get(5);
        assertEquals("Kotlin/ Java разработчик (middle/senior)", v6.title());
        assertEquals("X5 Tech", v6.company());
        assertNull(v6.salary());
        assertTrue(v6.link().startsWith("https://hh.ru/vacancy/127120812"));
    }
}

