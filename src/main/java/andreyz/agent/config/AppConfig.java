package andreyz.agent.config;

import andreyz.agent.service.resume.FileResumeSource;
import andreyz.agent.service.resume.ResumeSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.nio.file.Path;

@Configuration
public class AppConfig {

    @Bean
    public ResumeSource resumeSource() {
        return new FileResumeSource(Path.of("test/resume/andrey.txt"));
    }


    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
