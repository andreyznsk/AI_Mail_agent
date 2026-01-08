package andreyz.agent.persistence.repo;

import andreyz.agent.persistence.entity.CoverLetterSnapshotEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CoverLetterSnapshotRepository extends CrudRepository<CoverLetterSnapshotEntity, UUID> {

    Optional<CoverLetterSnapshotEntity> findFirstByVacancyId(UUID vacancyId);

}
