package andreyz.agent.persistence.repo;

import andreyz.agent.persistence.entity.VacancySnapshotEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface VacancySnapshotRepository extends CrudRepository<VacancySnapshotEntity, UUID> {

    boolean existsBySourceIdAndSource(String sourceId, String source);

}
