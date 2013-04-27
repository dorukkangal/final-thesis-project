package tr.edu.gsu.domain.message;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import tr.edu.gsu.domain.BaseEntity;
import tr.edu.gsu.domain.user.GoesUser;

@Entity
public class MessageMap extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	private GoesUser sender;

	@ManyToOne(fetch = FetchType.LAZY)
	private GoesUser receiver;

	private boolean readStatus = false;

	@ManyToOne(fetch = FetchType.LAZY)
	private MessageSource messageSource;

	public MessageMap(GoesUser sender, GoesUser receiver, boolean readStatus, MessageSource messageSource) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.readStatus = readStatus;
		this.messageSource = messageSource;
	}

	public MessageMap() {
	}

	public GoesUser getSender() {
		return sender;
	}

	public void setSender(GoesUser sender) {
		this.sender = sender;
	}

	public GoesUser getReceiver() {
		return receiver;
	}

	public void setReceiver(GoesUser receiver) {
		this.receiver = receiver;
	}

	public boolean isReadStatus() {
		return readStatus;
	}

	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
