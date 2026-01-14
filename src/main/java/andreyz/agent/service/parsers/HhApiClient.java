package andreyz.agent.service.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
public class HhApiClient {

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public HhApiClient() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public record VacancyContainer(String description, String area, String title, String employerTitle) { }

    /**
     * Получаем описание вакансии по vacancyId через HH API
     */
    public Optional<VacancyContainer> fetchVacancyDescription(String vacancyId) {
        String url = "https://api.hh.ru/vacancies/" + vacancyId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Java-HH-Parser/1.0") // HH требует User-Agent
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                String rawHtml = root.path("description").asText();
                // Убираем HTML-теги для удобного отображения
                String disc = Jsoup.parse(rawHtml).text();
                String areaName = root.path("area").path("name").asText(null); // вернёт null, если поле отсутствует
                log.debug("Vacancy area: {}", areaName);
                return Optional.of(new VacancyContainer(disc, areaName, root.path("name").asText(""), root.path("employer").path("name").asText("")));
            } else {
                log.error("HH API returned status {} for vacancy {}", response.statusCode(), vacancyId);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.getMessage(), e);
        }

        return Optional.empty();
    }
}
