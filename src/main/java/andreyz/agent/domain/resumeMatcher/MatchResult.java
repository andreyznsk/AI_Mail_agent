package andreyz.agent.domain.resumeMatcher;

import java.util.List;

public record MatchResult(
        int score,               // 0..100
        List<String> strengths,  // почему подхожу
        List<String> gaps,       // чего не хватает
        String summary           // краткое объяснение
) {}
