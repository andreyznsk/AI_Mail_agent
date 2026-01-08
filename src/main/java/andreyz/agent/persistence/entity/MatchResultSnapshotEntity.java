package andreyz.agent.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("match_result_snapshot")
@Data
@NoArgsConstructor
public class MatchResultSnapshotEntity {

    @Id
    private UUID id;

    @Column("vacancy_id")
    private UUID vacancyId;

    @Column("score")
    private int score;

    @Column("strengths")
    private PGobject strengthsJson;

    @Column("gaps")
    private PGobject gapsJson;

    @Column("summary")
    private String summary;

    @Column("matcher_version")
    private String matcherVersion;

    @Column("validation_passed")
    private boolean validationPassed;

    @Column("created_at")
    private OffsetDateTime createdAt;

    public MatchResultSnapshotEntity(
            UUID id,
            UUID vacancyId,
            int score,
            PGobject strengthsJson,
            PGobject gapsJson,
            String summary,
            String matcherVersion,
            boolean validationPassed,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.vacancyId = vacancyId;
        this.score = score;
        this.strengthsJson = strengthsJson;
        this.gapsJson = gapsJson;
        this.summary = summary;
        this.matcherVersion = matcherVersion;
        this.validationPassed = validationPassed;
        this.createdAt = createdAt;
    }

    // getters only
}
