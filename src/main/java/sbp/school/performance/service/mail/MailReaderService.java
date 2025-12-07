package sbp.school.performance.service.mail;

import sbp.school.performance.dto.MailItem;

import java.util.List;

public interface MailReaderService {

    List<MailItem> readInbox() throws Exception;

}
