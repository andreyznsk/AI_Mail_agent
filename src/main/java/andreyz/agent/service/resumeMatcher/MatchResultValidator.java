package andreyz.agent.service.resumeMatcher;

import andreyz.agent.domain.resumeMatcher.MatchResult;

public class MatchResultValidator {

    public static MatchResult validate(MatchResult result) {
        int score = Math.min(100, Math.max(0, result.score()));
        return new MatchResult(
            score,
            result.strengths(),
            result.gaps(),
            result.summary()
        );
    }
}
