package tr.edu.gsu.view.page.question;

import java.io.Serializable;

public interface QuestionComponent extends Serializable {

	public String getType();

	public byte[] getContent();

	public void setContent(byte[] contentType);

	public Boolean isTrue();

	public void setTrue(boolean isTrue);

	public String getMimeType();

	public float getWidth();

	public float getHeight();

	public boolean isResizeable();

	public void resize(int width, int height);

	public void setWidth(String height);

	public void setHeight(String height);

	public void focus();
}
