package andreyz.agent.domain.resume;

import lombok.Data;

import java.util.List;

@Data
public class Resume {

    private PersonalInfo personalInfo;
    private List<Experience> experience;
    private List<Skill> skills;
    private List<Education> education;

}