package sbp.school.performance.service.parsers;

import sbp.school.performance.dto.Vacancy;

import java.util.List;

public interface ParserService {
    List<Vacancy> parseVacancies(String html);
}
