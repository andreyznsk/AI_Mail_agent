package andreyz.agent.service.cacheResume;

import andreyz.agent.domain.resume.Resume;
import andreyz.agent.service.resume.ResumeParsingService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CachedResumeParsingService implements ResumeParsingService {

    private final ResumeParsingService llmResumeParsingService;
    private final ResumeCache cache;
    @SuppressWarnings("FieldCanBeLocal")
    private final MeterRegistry meterRegistry;
    private final Counter cacheHit;
    private final Counter cacheMiss;

    public CachedResumeParsingService(
            @Qualifier("llmResumeParsingService") ResumeParsingService llmResumeParsingService,
            ResumeCache cache,
            MeterRegistry meterRegistry) {
        this.llmResumeParsingService = llmResumeParsingService;
        this.cache = cache;
        this.meterRegistry = meterRegistry;

        this.cacheHit = meterRegistry.counter("resume.cache.hit");
        this.cacheMiss = meterRegistry.counter("resume.cache.miss");
    }

    @Override
    public Resume parse(String rawText) {
        String hash = ContentHashCalculator.sha256(rawText);

        Optional<Resume> cached = cache.get(hash);
        if (cached.isPresent()) {
            cacheHit.increment();
            return cached.get();
        } else {
            log.warn("Cache mismatch!");
            cacheMiss.increment();
            Resume resume = llmResumeParsingService.parse(rawText);
            cache.put(hash, resume);
            return resume;
        }
    }
}
