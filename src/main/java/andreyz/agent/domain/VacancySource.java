package andreyz.agent.domain;

import lombok.Getter;

@Getter
public enum VacancySource {

    HH("HeadHunter");

    private final String name;

    VacancySource(String name) {
        this.name = name;
    }

}
