package tr.edu.gsu.domain.exam;

import javax.persistence.Entity;
import javax.persistence.Lob;

import tr.edu.gsu.domain.BaseEntity;

@Entity
public class QuestionField extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String type;

	@Lob
	private byte[] content;

	private Boolean isTrue;

	private String mimeType;

	private String distanceFromTop;

	private String distanceFromLeft;

	private String width;

	private String height;

	public QuestionField(String type, byte[] content, Boolean isTrue, String mimeType, String distanceFromTop, String distanceFromLeft, String width, String height) {
		super();
		this.type = type;
		this.content = content;
		this.isTrue = isTrue;
		this.mimeType = mimeType;
		this.distanceFromTop = distanceFromTop;
		this.distanceFromLeft = distanceFromLeft;
		this.width = width;
		this.height = height;
	}

	public QuestionField() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Boolean isTrue() {
		return isTrue;
	}

	public void setIsTrue(Boolean isTrue) {
		this.isTrue = isTrue;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getDistanceFromTop() {
		return distanceFromTop;
	}

	public void setDistanceFromTop(String distanceFromTop) {
		this.distanceFromTop = distanceFromTop;
	}

	public String getDistanceFromLeft() {
		return distanceFromLeft;
	}

	public void setDistanceFromLeft(String distanceFromLeft) {
		this.distanceFromLeft = distanceFromLeft;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return type;
	}
}
