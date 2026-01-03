package andreyz.agent.service.cacheResume;

import andreyz.agent.domain.resume.Resume;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileResumeCache implements ResumeCache {


    @Value("${cache.resumeDir:N/A}")
    private String cacheDirStr;

    private Path cacheDir;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init(){
        cacheDir = Path.of(cacheDirStr);
    }


    @Override
    public Optional<Resume> get(String contentHash) {
        Path file = cacheDir.resolve("resume_" + contentHash + ".json");

        if (!Files.exists(file)) {
            return Optional.empty();
        }

        try {
            return Optional.of(
                objectMapper.readValue(file.toFile(), Resume.class)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resume cache", e);
        }
    }

    @Override
    public void put(String contentHash, Resume resume) {
        try {
            Files.createDirectories(cacheDir);
            Path file = cacheDir.resolve("resume_" + contentHash + ".json");
            objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(file.toFile(), resume);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write resume cache", e);
        }
    }
}
