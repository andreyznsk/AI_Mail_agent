package andreyz.agent.service.CLGenerator;

import java.util.Locale;

public enum CoverLetterLanguage {
    ENGLISH("English"),
    RUSSIAN("Russian"),
    GERMAN("German");

    private final String promptName;

    CoverLetterLanguage(String promptName) {
        this.promptName = promptName;
    }

    public String promptName() {
        return promptName;
    }

    public static CoverLetterLanguage fromLocale(Locale locale) {
        return switch (locale.getLanguage()) {
            case "ru" -> RUSSIAN;
            case "de" -> GERMAN;
            default -> ENGLISH;
        };
    }
}
