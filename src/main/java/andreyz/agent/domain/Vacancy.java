package andreyz.agent.domain;

/**
 * Модель для хранения данных о вакансии
 *
 * @param title Getters
 */

public record Vacancy(String title, String company, Long salary, String link, String vacancyId, String description, String area) {

}