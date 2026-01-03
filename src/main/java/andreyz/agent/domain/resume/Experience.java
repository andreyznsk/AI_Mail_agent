package andreyz.agent.domain.resume;

import java.util.List;

public record Experience(
    String company,
    String role,
    String period,
    List<String> responsibilities,
    List<String> technologies
) {}