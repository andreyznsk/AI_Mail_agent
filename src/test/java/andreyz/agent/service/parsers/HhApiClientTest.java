package andreyz.agent.service.parsers;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
class HhApiClientTest {

    private final HhApiClient hhApiClient = new HhApiClient();

//    Only for manual use
//    @Test
    void fetchVacancyDescription_Success() {
        // When
        Optional<HhApiClient.VacancyContainer> result = hhApiClient.fetchVacancyDescription("129412001");
        log.info("\nEmployer name: {} \nVacancy title: {}\n\nVacancy description: {}", result.get().employerTitle(), result.get().title(), result.get().description());
    }

}