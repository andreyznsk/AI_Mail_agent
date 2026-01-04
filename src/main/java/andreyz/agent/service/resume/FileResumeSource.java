package andreyz.agent.service.resume;

import andreyz.agent.exception.ResumeLoadingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileResumeSource implements ResumeSource {

    private final Path filePath;

    public FileResumeSource(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public String load() {
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new ResumeLoadingException("Failed to load resume from file: " + filePath, e);
        }
    }
}
