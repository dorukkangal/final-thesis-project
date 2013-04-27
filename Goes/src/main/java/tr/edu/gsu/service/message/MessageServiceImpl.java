package tr.edu.gsu.service.message;

import tr.edu.gsu.domain.message.MessageMap;
import tr.edu.gsu.service.BaseServiceImpl;

public class MessageServiceImpl extends BaseServiceImpl<MessageMap> implements MessageService {
	private static final long serialVersionUID = 1L;

	public MessageServiceImpl() {
		super(MessageMap.class);
	}
}
