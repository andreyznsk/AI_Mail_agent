package andreyz.agent.service.parsers;


import andreyz.agent.domain.Vacancy;
import andreyz.agent.domain.VacancySource;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ParserServiceHeadHunter implements ParserService {

    private static final Pattern pattern = Pattern.compile("\\d+");
    private static final Pattern VACANCY_ID_PATTERN = Pattern.compile("/vacancy/(\\d+)");

    private final HhApiClient hhApiClient;


    public static String extractVacancyId(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        Matcher matcher = VACANCY_ID_PATTERN.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    @Override
    public List<Vacancy> parseVacancies(String html) {
        List<Vacancy> vacancies = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        // Находим все строки с вакансиями (каждая в <tr> с border-top)
        Elements vacancyRows = doc.select("td[style*=\"border-top:1px solid #ededed\"]");

        for (Element row : vacancyRows) {
            Long salary = null;
            String link = "";

            // Извлекаем заголовок и ссылку
            Element titleLink = row.select("a[target=_blank]").first();
            if (titleLink != null) {
                titleLink.text();
                link = titleLink.attr("href").trim();
            }

            // Извлекаем зарплату (если есть)
            Element salaryEl = row.select("td.salary").first();
            if (salaryEl != null) {
                String rawSalary = salaryEl.text();
                // Извлекаем все цифры из строки (например, "от 160000 ₽ за месяц" → "160000")
                Matcher matcher = pattern.matcher(rawSalary);
                if (matcher.find()) {
                    try {
                        salary = Long.parseLong(matcher.group());
                    } catch (NumberFormatException ignored) {}
                }
            }


            String vacancyId = extractVacancyId(link).trim();

            Optional<HhApiClient.VacancyContainer> vacancyDescription = hhApiClient.fetchVacancyDescription(vacancyId);
            if (vacancyDescription.isPresent()) {
                vacancies.add(new Vacancy(vacancyDescription.get().title(), vacancyDescription.get().employerTitle(), salary, link, vacancyId, vacancyDescription.get().description(), vacancyDescription.get().area(), VacancySource.HH));
            }
        }

        return vacancies;
    }
}
