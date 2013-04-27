package tr.edu.gsu.view.page.question.component;

import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.edu.gsu.util.AudioUtil;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.page.question.QuestionComponent;
import tr.edu.gsu.view.upload.UploadComponent;

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Audio;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class QuestionSound extends CustomComponent implements QuestionComponent {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(QuestionSound.class);

	private static final int DEFAULT_WIDTH = 320;
	private static final int DEFAULT_HEIGHT = 150;

	private VerticalLayout mainLayout;
	private HorizontalLayout uploadLayout;
	private UploadComponent uploadComponent;
	private VerticalLayout displayLayout;
	private Audio audioContainer;

	private byte[] audio;
	private String mimeType = null;

	public QuestionSound() {
		resize(-1, -1);

		mainLayout = buildMainLayout();
		setCompositionRoot(mainLayout);
		startMode();
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		uploadLayout = buildUploadLayout();
		mainLayout.addComponent(uploadLayout);

		displayLayout = buildDisplayLayout();
		mainLayout.addComponent(displayLayout);

		return mainLayout;
	}

	private HorizontalLayout buildUploadLayout() {
		uploadLayout = new HorizontalLayout();
		uploadLayout.setSizeFull();

		uploadComponent = buildUploadComponent();
		uploadLayout.addComponent(uploadComponent);
		uploadLayout.setComponentAlignment(uploadComponent, Alignment.MIDDLE_CENTER);

		return uploadLayout;
	}

	private UploadComponent buildUploadComponent() {
		uploadComponent = new UploadComponent(GoesConstants.ACCEPTED_AUDIO_MIMETYPES);
		SucceededListener succeededListener = new SucceededListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				try {
					AudioInputStream audioIStream;
					try {
						mimeType = event.getMIMEType();
						audioIStream = AudioSystem.getAudioInputStream(uploadComponent.getFile());
						audio = new byte[audioIStream.available()];
						audioIStream.read(audio);
						setContent(audio);
					} catch (UnsupportedAudioFileException e) {
						logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		};
		uploadComponent.addSucceededListeners(Arrays.asList(succeededListener));

		return uploadComponent;
	}

	private VerticalLayout buildDisplayLayout() {
		displayLayout = new VerticalLayout();
		displayLayout.setSizeFull();

		audioContainer = buildAudioContainer();
		displayLayout.addComponent(audioContainer);
		displayLayout.setExpandRatio(audioContainer, 1.0f);

		return displayLayout;
	}

	private Audio buildAudioContainer() {
		audioContainer = new Audio();
		audioContainer.setSizeUndefined();
		audioContainer.setImmediate(true);

		return audioContainer;
	}

	private void startMode() {
		mainLayout.removeAllComponents();
		addUploadLayout();
	}

	private void displayMode() {
		mainLayout.removeAllComponents();
		addDisplayLayout();
	}

	private void addUploadLayout() {
		mainLayout.addComponent(uploadLayout);
	}

	private void addDisplayLayout() {
		mainLayout.addComponent(displayLayout);
	}

	public void removeAudio() {
		audioContainer.setSource(null);
	}

	@Override
	public String getType() {
		return GoesConstants.QUESTION_SOUND;
	}

	@Override
	public byte[] getContent() {
		return audio;
	}

	@Override
	public void setContent(byte[] audio) {
		this.audio = audio;
		if (audio != null) {
			StreamResource imageResource = AudioUtil.convertBytes2StreamResource(audio);
			audioContainer.setSource(imageResource);
			setSizeUndefined();
			displayMode();
		} else
			startMode();
	}

	@Override
	public Boolean isTrue() {
		return false;
	}

	@Override
	public void setTrue(boolean isTrue) {

	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public boolean isResizeable() {
		return false;
	}

	@Override
	public void resize(int width, int height) {
		setWidth(DEFAULT_WIDTH, Sizeable.UNITS_PIXELS); // UNITS_PIXEL
		setHeight(DEFAULT_HEIGHT, Sizeable.UNITS_PIXELS); // UNITS_PIXEL
	}

	@Override
	public void focus() {
		uploadComponent.focus();
	}
}