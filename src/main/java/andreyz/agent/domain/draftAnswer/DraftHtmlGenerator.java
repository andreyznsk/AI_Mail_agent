package andreyz.agent.domain.draftAnswer;

import java.util.List;

public class DraftHtmlGenerator {

    public static String buildVacancyTableHtml(List<VacancyCoverLetter> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Simple HTML Page</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 20px;
                            background-color: #f4f4f4;
                        }
                        .container {
                            max-width: 800px;
                            margin: 0 auto;
                            padding: 20px;
                            background-color: #fff;
                            border-radius: 8px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        }
                        h1 {
                            color: #333;
                        }
                        p {
                            color: #666;
                        }
                    </style>
                </head>
                <body>
                """);
        sb.append("<table style='border-collapse: collapse; width: 100%; border: 1px solid #ddd;'>");
        sb.append("<tr>");
        sb.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Vacancy</th>");
        sb.append("<th style='border: 1px solid #ddd; padding: 8px; text-align: left;'>Cover Letter</th>");
        sb.append("</tr>");
        for (VacancyCoverLetter item : data) {
            sb.append("<tr>");
            sb.append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append("<a style='color: #0070FF; text-decoration: none;' target='_blank' href='")
                    .append(item.vacancyUrl())
                    .append("'>")
                    .append(item.vacancyTitle())
                    .append("</a>")
                    .append("</td>");
            sb.append("<td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append(item.coverLetterText())
                    .append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("</body></html>");
        return sb.toString();
    }

}
