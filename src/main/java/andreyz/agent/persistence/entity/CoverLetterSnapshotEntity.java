package andreyz.agent.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("cover_letter_snapshot")
@Data
@NoArgsConstructor
public class CoverLetterSnapshotEntity {

    @Id
    private UUID id;

    @Column("vacancy_id")
    private UUID vacancyId;

    @Column("cover_letter_text")
    private String coverLetterText;

    @Column("llm_provider")
    private String llmProvider;

    @Column("prompt_version")
    private String promptVersion;

    @Column("created_at")
    private OffsetDateTime createdAt;

    public CoverLetterSnapshotEntity(
            UUID id,
            UUID vacancyId,
            String coverLetterText,
            String llmProvider,
            String promptVersion,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.vacancyId = vacancyId;
        this.coverLetterText = coverLetterText;
        this.llmProvider = llmProvider;
        this.promptVersion = promptVersion;
        this.createdAt = createdAt;
    }

    // getters only
}
