package tr.edu.gsu.view.page.question.component;

import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.util.ImageUtil;
import tr.edu.gsu.view.page.question.QuestionComponent;
import tr.edu.gsu.view.upload.UploadComponent;

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class QuestionImage extends CustomComponent implements QuestionComponent {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(QuestionImage.class);

	private static final int DEFAULT_WIDTH = 250;
	private static final int DEFAULT_HEIGHT = 150;

	private VerticalLayout mainLayout;
	private HorizontalLayout uploadLayout;
	private UploadComponent uploadComponent;
	private VerticalLayout displayLayout;
	private Embedded imageContainer;

	private byte[] image;
	private String mimeType = null;

	public QuestionImage() {
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
		uploadComponent = new UploadComponent(GoesConstants.ACCEPTED_IMAGE_MIMETYPES);
		SucceededListener succeededListener = new SucceededListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				try {
					mimeType = event.getMIMEType();
					ImageIO.write(ImageIO.read(uploadComponent.getFile()), mimeType, uploadComponent.getFile());
					image = FileUtils.readFileToByteArray(uploadComponent.getFile());
					setContent(image);
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

		imageContainer = buildImageContainer();
		displayLayout.addComponent(imageContainer);
		displayLayout.setExpandRatio(imageContainer, 1.0f);

		return displayLayout;
	}

	private Embedded buildImageContainer() {
		imageContainer = new Embedded();
		imageContainer.setSizeUndefined();
		imageContainer.setImmediate(true);

		return imageContainer;
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

	public void removeImage() {
		imageContainer.setSource(null);
	}

	@Override
	public String getType() {
		return GoesConstants.QUESTION_IMAGE;
	}

	@Override
	public byte[] getContent() {
		return image;
	}

	@Override
	public void setContent(byte[] image) {
		this.image = image;
		if (image != null) {
			StreamResource imageResource = ImageUtil.resizeImage(image, ((int) getWidth()), ((int) getHeight()), true);
			imageContainer.setSource(imageResource);
			setSizeUndefined();
			displayMode();
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
		return mimeType;
	}

	@Override
	public boolean isResizeable() {
		return true;
	}

	@Override
	public void resize(int width, int height) {
		setWidth((width == -1) ? DEFAULT_WIDTH : width, Sizeable.UNITS_PIXELS); // UNITS_PIXEL
		setHeight((height == -1) ? DEFAULT_HEIGHT : height, Sizeable.UNITS_PIXELS); // UNITS_PIXEL
		if (getContent() != null)
			setContent(getContent());
	}

	@Override
	public void focus() {
		uploadComponent.focus();
	}
}
