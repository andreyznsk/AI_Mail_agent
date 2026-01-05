package andreyz.agent.persistence.mapper;

import andreyz.agent.domain.CoverLetter.CoverLetterResponse;
import andreyz.agent.persistence.entity.CoverLetterSnapshotEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CoverLetterMapper {

    public static CoverLetterSnapshotEntity toEntity(
            UUID vacancyId,
            CoverLetterResponse coverLetter,
            String llmProvider,
            String promptVersion,
            OffsetDateTime createdAt
    ) {
        return new CoverLetterSnapshotEntity(
                null,
                vacancyId,
                coverLetter.text(),
                llmProvider,
                promptVersion,
                createdAt
        );
    }
}
