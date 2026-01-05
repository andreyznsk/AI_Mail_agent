package andreyz.agent.persistence.mapper;

import andreyz.agent.domain.resumeMatcher.MatchResult;
import andreyz.agent.persistence.entity.MatchResultSnapshotEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.UUID;

import static andreyz.agent.persistence.utils.PGobjectUtil.toJsonb;

public class MatchResultMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static MatchResultSnapshotEntity toEntity(
            UUID vacancyId,
            MatchResult result,
            String matcherVersion,
            OffsetDateTime createdAt

    ) {
        try {
            return new MatchResultSnapshotEntity(
                    null,
                    vacancyId,
                    result.score(),
                    toJsonb(result.strengths()),
                    toJsonb(result.gaps()),
                    result.summary(),
                    matcherVersion,
                    true,
                    createdAt
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize MatchResult", e);
        }
    }
}
