package andreyz.agent.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

public record MailItem(String id, String subject, String body, ParserServiceType parserServiceType, ZonedDateTime originalDateTime) implements Serializable {

}
