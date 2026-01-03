package andreyz.agent.service.llm;

public interface LlmClient {

    String PROMPT = "Ответь одним предложением: какая модель ты и от какого разработчика?";

    String complete(String prompt);
}
