
package andreyz.agent.service.resume;

import andreyz.agent.domain.resume.Resume;

public interface ResumeParsingService {
    Resume parse(String rawResumeText);
}