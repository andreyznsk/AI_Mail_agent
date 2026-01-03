package andreyz.agent.service.mail;

import andreyz.agent.dto.MailItem;

import java.util.List;

public interface MailReaderService {

    List<MailItem> readInbox() throws Exception;

}
