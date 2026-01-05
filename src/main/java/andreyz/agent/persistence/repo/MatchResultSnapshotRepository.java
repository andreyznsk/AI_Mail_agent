package andreyz.agent.persistence.repo;

import andreyz.agent.persistence.entity.MatchResultSnapshotEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MatchResultSnapshotRepository
        extends CrudRepository<MatchResultSnapshotEntity, UUID> {
}
