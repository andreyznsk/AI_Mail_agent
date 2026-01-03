package andreyz.agent.service.cacheResume;

import andreyz.agent.domain.resume.Resume;
import andreyz.agent.service.resume.ResumeParsingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CachedResumeParsingService implements ResumeParsingService {

    private final ResumeParsingService LlmResumeParsingService;
    private final ResumeCache cache;

    public CachedResumeParsingService(
            @Qualifier("llmResumeParsingService") ResumeParsingService llmResumeParsingService,
            ResumeCache cache) {
        LlmResumeParsingService = llmResumeParsingService;
        this.cache = cache;
    }

    @Override
    public Resume parse(String rawText) {
        String hash = ContentHashCalculator.sha256(rawText);

        return cache.get(hash)
            .orElseGet(() -> {
                log.warn("Cache mismatch!");
                Resume resume = LlmResumeParsingService.parse(rawText);
                cache.put(hash, resume);
                return resume;
            });
    }
}
