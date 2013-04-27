package tr.edu.gsu.view.page.question.component;

import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.page.question.QuestionComponent;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;

public class QuestionText extends CustomComponent implements QuestionComponent {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_NAME = "New Question";
	private static final int DEFAULT_WIDTH = 150;
	private static final int DEFAULT_HEIGHT = 30;

	private HorizontalLayout mainLayout;
	private AbstractComponent body;

	private byte[] content;
	private boolean editMode;

	public QuestionText(boolean editMode) {
		this(DEFAULT_NAME, editMode);
	}

	public QuestionText(String value, boolean editMode) {
		super();
		this.editMode = editMode;

		mainLayout = buildMainLayout();
		setCompositionRoot(mainLayout);
		resize(-1, -1);
		setImmediate(true);
		setContent(value.getBytes());
	}

	private HorizontalLayout buildMainLayout() {
		mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();

		body = buildBody();
		mainLayout.addComponent(body);
		mainLayout.setExpandRatio(body, 1.0f);

		return mainLayout;
	}

	private AbstractComponent buildBody() {
		if (editMode) {
			TextArea t = new TextArea();
			t.setImmediate(true);
			t.setRequired(true);
			t.setNullRepresentation("");
			t.focus();
			body = t;
		} else {
			Label l = new Label();
			l.setContentMode(Label.CONTENT_XHTML);
			body = l;
		}
		body.setSizeFull();
		return body;
	}

	@Override
	public String getType() {
		return GoesConstants.QUESTION_TEXT;
	}

	@Override
	public byte[] getContent() {
		String value = "";
		if (editMode)
			value = (String) ((TextArea) body).getValue();
		else
			value = (String) ((Label) body).getValue();
		content = (value == null) ? "".getBytes() : value.getBytes();
		return content;
	}

	@Override
	public void setContent(byte[] content) {
		this.content = content;
		if (content != null) {
			if (body instanceof Label)
				((Label) body).setValue(new String(content));
			else if (body instanceof TextArea)
				((TextArea) body).setValue(new String(content));
		}
	}

	@Override
	public Boolean isTrue() {
		return null;
	}

	@Override
	public void setTrue(boolean isTrue) {

	}

	@Override
	public String getMimeType() {
		return null;
	}

	@Override
	public boolean isResizeable() {
		return true;
	}

	@Override
	public void resize(int width, int height) {
		setWidth((width == -1) ? DEFAULT_WIDTH : width, Sizeable.UNITS_PIXELS); // UNITS_PIXEL
		setHeight((height == -1) ? DEFAULT_HEIGHT : height, Sizeable.UNITS_PIXELS); // UNITS_PIXEL
	}

	@Override
	public void focus() {
		if (body instanceof TextArea)
			((TextArea) body).focus();
	}
}
