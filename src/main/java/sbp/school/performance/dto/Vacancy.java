package sbp.school.performance.dto;

import lombok.ToString;

/**
 * Модель для хранения данных о вакансии
 *
 * @param title Getters
 */

public record Vacancy(String title, String company, Long salary, String link) {

}