package andreyz.agent.persistence.mapper;

import andreyz.agent.domain.Vacancy;
import andreyz.agent.persistence.entity.VacancySnapshotEntity;

import java.time.OffsetDateTime;

public class VacancyMapper {

    public static VacancySnapshotEntity toEntity(
            Vacancy vacancy,
            String source,
            OffsetDateTime receivedAt
    ) {
        return new VacancySnapshotEntity(
                null,
                vacancy.company(),
                vacancy.title(),
                vacancy.salary() == null ? 0L : vacancy.salary(),
                vacancy.area(),
                vacancy.description(),
                source,
                receivedAt,
                receivedAt
        );
    }
}
