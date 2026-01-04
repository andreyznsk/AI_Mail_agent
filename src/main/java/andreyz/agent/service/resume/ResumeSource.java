package andreyz.agent.service.resume;

public interface ResumeSource {

    /**
     * @return raw resume content (text)
     */
    String load();
}
