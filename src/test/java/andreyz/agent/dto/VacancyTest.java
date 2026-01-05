package andreyz.agent.dto;

import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.VacancySource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class VacancyTest {

    @Test
    void vacancyTest() {
        Vacancy vacancy = new Vacancy("test", "com", 10L, "link" , "128148220" , "test", "tomsk", VacancySource.HH);
        log.info("{}", vacancy);
        assertNotNull(vacancy);
    }

}