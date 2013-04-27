package tr.edu.gsu.domain.message;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import tr.edu.gsu.domain.BaseEntity;

@Entity
public class MessageSource extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String messageSubject;

	@Lob
	private byte[] messageBody;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "messageSource")
	private Set<MessageMap> userMessageMaps;

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public byte[] getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(byte[] messageBody) {
		this.messageBody = messageBody;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Set<MessageMap> getUserMessageMaps() {
		return userMessageMaps;
	}

	public void setUserMessageMaps(Set<MessageMap> userMessageMaps) {
		this.userMessageMaps = userMessageMaps;
	}
}
