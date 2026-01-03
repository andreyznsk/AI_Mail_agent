package andreyz.agent.service.parsers;


import andreyz.agent.domain.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParserServiceHeadHunter implements ParserService {

    private static final Pattern pattern = Pattern.compile("\\d+");

    private static final Pattern VACANCY_ID_PATTERN = Pattern.compile("/vacancy/(\\d+)");

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
            String title = "";
            String company = "";
            Long salary = null;
            String link = "";

            // Извлекаем заголовок и ссылку
            Element titleLink = row.select("a[target=_blank]").first();
            if (titleLink != null) {
                title = titleLink.text().trim();
                link = titleLink.attr("href").trim();
            }

            // 2. Извлекаем компанию: второй <tr> внутри блока, ищем p[data-qa=mail__text] в нём
            Elements rows = row.select("table");
            if (rows.size() >= 3) {
                Element secondRow = rows.get(2); // Второй <tr> — это компания
                Element companyEl = secondRow.select("p[data-qa=mail__text]").first();
                if (companyEl != null) {
                    company = companyEl.text().trim();
                }
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





            vacancies.add(new Vacancy(title, company, salary, link, extractVacancyId(link).trim()));
        }

        return vacancies;
    }
}
