package tr.edu.gsu.view.page.question.component;

import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.page.question.QuestionComponent;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class QuestionVideo extends CustomComponent implements QuestionComponent {

	private static final int DEFAULT_WIDTH = 360;
	private static final int DEFAULT_HEIGHT = 300;

	private VerticalLayout mainLayout;
	private HorizontalLayout entryLayout;
	private TextField videoIdField;
	private VerticalLayout displayLayout;
	private Embedded videoContainer;

	private byte[] videoId;

	private Button okButton;

	public QuestionVideo() {
		mainLayout = buildMainLayout();
		setCompositionRoot(mainLayout);
		startMode();
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		entryLayout = buildEntryLayout();
		mainLayout.addComponent(entryLayout);

		displayLayout = buildDisplayLayout();
		mainLayout.addComponent(displayLayout);

		return mainLayout;
	}

	private HorizontalLayout buildEntryLayout() {
		entryLayout = new HorizontalLayout();
		// entryLayout.setSizeFull();

		videoIdField = buildVideoIdFieldComponent();
		entryLayout.addComponent(videoIdField);

		okButton = buildOkButton();
		entryLayout.addComponent(okButton);

		return entryLayout;
	}

	private TextField buildVideoIdFieldComponent() {
		videoIdField = new TextField();
		videoIdField.setInputPrompt("Youtube video id");
		videoIdField.setImmediate(true);
		videoIdField.setNullRepresentation("");
		videoIdField.focus();
		return videoIdField;
	}

	private Button buildOkButton() {
		okButton = new Button(Messages.getValue(GoesConstants.BUTTON_OK), new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String videoId = (String) videoIdField.getValue();
				if (videoId == null || "".equals(videoId))
					return;
				setContent(videoId.getBytes());
			}
		});
		okButton.setClickShortcut(KeyCode.ENTER);
		return okButton;
	}

	private VerticalLayout buildDisplayLayout() {
		displayLayout = new VerticalLayout();
		displayLayout.setSizeFull();

		videoContainer = buildVideoContainer();
		displayLayout.addComponent(videoContainer);
		displayLayout.setExpandRatio(videoContainer, 1.0f);

		return displayLayout;
	}

	private Embedded buildVideoContainer() {
		videoContainer = new Embedded();
		videoContainer.setSizeFull();
		videoContainer.setImmediate(true);
		videoContainer.setMimeType(getMimeType());
		videoContainer.setParameter("allowFullScreen", "true");

		return videoContainer;
	}

	private void startMode() {
		mainLayout.removeAllComponents();
		addEntryLayout();
	}

	private void displayMode() {
		mainLayout.removeAllComponents();
		addDisplayLayout();
	}

	private void addEntryLayout() {
		mainLayout.addComponent(entryLayout);
	}

	private void addDisplayLayout() {
		mainLayout.addComponent(displayLayout);
	}
	
	private void showVideo(String videoId) {
		videoContainer.setSource(new ExternalResource(GoesConstants.YOUTUBE_PROTOCOL + videoId));
	}

	public void removeVideo() {
		videoContainer.setSource(null);
	}

	@Override
	public String getType() {
		return GoesConstants.QUESTION_VIDEO;
	}

	@Override
	public byte[] getContent() {
		return videoId;
	}

	@Override
	public void setContent(byte[] videoId) {
		this.videoId = videoId;
		if (videoId != null) {
			showVideo(new String(videoId));
			displayMode();
			resize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		} else
			startMode();
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
		return GoesConstants.MIMETYPE_FLASH_VIDEO;
	}

	@Override
	public boolean isResizeable() {
		return true;
	}

	@Override
	public void resize(int width, int height) {
		if (videoContainer != null && videoContainer.getSource() != null) {
			setWidth((width == -1) ? DEFAULT_WIDTH : width, Sizeable.UNITS_PIXELS); // UNITS_PIXEL
			setHeight((height == -1) ? DEFAULT_HEIGHT : height, Sizeable.UNITS_PIXELS); // UNITS_PIXEL
			showVideo(new String(videoId));
		}
	}

	@Override
	public void focus() {
		videoIdField.focus();
	}
}
