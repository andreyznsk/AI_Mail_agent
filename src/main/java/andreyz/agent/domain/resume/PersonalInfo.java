package andreyz.agent.domain.resume;

public record PersonalInfo(
    String fullName,
    String currentTitle,
    int yearsOfExperience,
    String location
) {}