package sbp.school.performance.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class VacancyTest {

    @Test
    void vacancyTest() {
        Vacancy vacancy = new Vacancy("test", "com", 10L, "link" , "128148220");
        log.info("{}", vacancy);
        assertNotNull(vacancy);
    }

}