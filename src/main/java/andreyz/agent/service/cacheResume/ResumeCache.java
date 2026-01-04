package andreyz.agent.service.cacheResume;

import andreyz.agent.domain.resume.Resume;

import java.util.Optional;

public interface ResumeCache {

    Optional<Resume> get(String contentHash);

    void put(String contentHash, Resume resume);
}
