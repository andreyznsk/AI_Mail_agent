package andreyz.agent.service.parsers;

import andreyz.agent.domain.Vacancy;

import java.util.List;

public interface ParserService {
    List<Vacancy> parseVacancies(String html);
}
