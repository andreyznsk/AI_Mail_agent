package andreyz.agent.service.mail;

import andreyz.agent.domain.draftAnswer.DraftEmailRequest;
import andreyz.agent.dto.MailItem;

import java.util.List;

public interface MailReaderService {

    List<MailItem> readInbox() throws Exception;

    void markAsRead(String originalMessageId);

    void createDraftEmail(DraftEmailRequest request);

}
