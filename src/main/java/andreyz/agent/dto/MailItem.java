package andreyz.agent.dto;

import java.io.Serializable;

public record MailItem(String id, String subject, String body, ParserServiceType parserServiceType) implements Serializable {

}
