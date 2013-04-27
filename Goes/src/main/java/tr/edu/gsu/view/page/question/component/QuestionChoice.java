package tr.edu.gsu.view.page.question.component;

import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.page.question.QuestionComponent;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;

public class QuestionChoice extends CustomComponent implements QuestionComponent {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_NAME = "New Choice";
	private static final int DEFAULT_WIDTH = 150;
	private static final int DEFAULT_HEIGHT = 30;

	private Panel container;
	private HorizontalLayout mainLayout;
	private AbstractComponent body;
	private CheckBox isTrueBox;

	private byte[] content;
	private boolean editMode;

	public QuestionChoice(boolean editMode) {
		this(DEFAULT_NAME, editMode);
	}

	public QuestionChoice(String value, boolean editMode) {
		super();
		this.editMode = editMode;

		container = buildContainer();
		setCompositionRoot(container);
		resize(-1, -1);
		setImmediate(true);
		setContent(value.getBytes());
	}

	private Panel buildContainer() {
		container = new Panel();
		container.setSizeFull();

		mainLayout = buildMainLayout();
		container.setContent(mainLayout);

		return container;
	}

	private HorizontalLayout buildMainLayout() {
		mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();

		body = buildBody();
		mainLayout.addComponent(body);
		mainLayout.setExpandRatio(body, 1.0f);

		isTrueBox = buildIsTrueBox();
		mainLayout.addComponent(isTrueBox);

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

	private CheckBox buildIsTrueBox() {
		isTrueBox = new CheckBox();
		isTrueBox.setReadOnly(!editMode);
		return isTrueBox;
	}

	@Override
	public String getType() {
		return GoesConstants.QUESTION_CHOICE;
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
			if (editMode)
				((TextArea) body).setValue(new String(content));
			else
				((Label) body).setValue(new String(content));
		}
	}

	@Override
	public Boolean isTrue() {
		Object value = isTrueBox.getValue();
		return ((value != null && value instanceof Boolean) ? (boolean) value : false);
	}

	@Override
	public void setTrue(boolean isTrue) {
		isTrueBox.setReadOnly(false);
		isTrueBox.setValue(isTrue);
		isTrueBox.setReadOnly(!editMode);
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
