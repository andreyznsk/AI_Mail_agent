package andreyz.agent.domain.draftAnswer;

public record DraftEmailRequest (
    String to,            // получатель
    String subject,       // тема письма
    String htmlBody)      // HTML с таблицей вакансий + CL
{}
