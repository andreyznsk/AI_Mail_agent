package andreyz.agent.service.mail;

import andreyz.agent.domain.MailItem;

import java.util.List;

public interface MailReaderService {

    List<MailItem> readInbox() throws Exception;

}
