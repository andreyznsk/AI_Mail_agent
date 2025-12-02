package sbp.school.performance.service;

import sbp.school.performance.dto.MailItem;

import java.util.List;

public interface MailReaderService {

    List<MailItem> readInbox() throws Exception;

}
