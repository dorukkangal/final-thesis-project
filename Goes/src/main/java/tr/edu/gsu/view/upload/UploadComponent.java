package tr.edu.gsu.view.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.StreamVariable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class UploadComponent extends VerticalLayout implements ClickListener, DropHandler, Receiver, StartedListener, FinishedListener, SucceededListener,
		FailedListener, ProgressListener {

	private VerticalLayout startLayout;
	private Upload upload;
	private Label orLabel;
	private DragAndDropWrapper dropFileComponent;

	private VerticalLayout uploadLayout;
	private Label pleaseWaitLabel;
	private Label descriptionLabel;
	private HorizontalLayout progressLayout;
	private ProgressIndicator progressIndicator;
	private Button cancelButton;

	private File file;

	private List<String> acceptedMimeTypes = new ArrayList<String>();

	private List<SucceededListener> succeededListeners = new ArrayList<SucceededListener>();
	private List<FailedListener> failedListeners = new ArrayList<FailedListener>();

	public UploadComponent(List<String> acceptedMimeTypes) {
		this.acceptedMimeTypes = acceptedMimeTypes;
		init();
		startMode();
		setSizeFull();
	}

	private void init() {
		startLayout = buildStartLayout();
		addComponent(startLayout);

		uploadLayout = buildUploadLayout();
		addComponent(uploadLayout);
	}

	private VerticalLayout buildStartLayout() {
		startLayout = new VerticalLayout();
		startLayout.setSizeFull();

		upload = buildUpload();
		startLayout.addComponent(upload);
		startLayout.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);

		orLabel = buildOrLabel();
		startLayout.addComponent(orLabel);
		startLayout.setComponentAlignment(orLabel, Alignment.MIDDLE_CENTER);

		dropFileComponent = buildDropFileComponent();
		startLayout.addComponent(dropFileComponent);
		startLayout.setComponentAlignment(dropFileComponent, Alignment.MIDDLE_CENTER);

		return startLayout;
	}

	private Upload buildUpload() {
		upload = new Upload();
		upload.setButtonCaption(Messages.getValue(GoesConstants.MESSAGE_SELECT));
		upload.setImmediate(true);

		upload.setReceiver((Upload.Receiver) this);
		upload.addListener((Upload.StartedListener) this);
		upload.addListener((Upload.ProgressListener) this);
		upload.addListener((Upload.FailedListener) this);
		upload.addListener((Upload.FinishedListener) this);
		upload.addListener((Upload.SucceededListener) this);

		return upload;
	}

	protected Label buildOrLabel() {
		orLabel = new Label(Messages.getValue(GoesConstants.MESSAGE_OR));
		orLabel.setSizeUndefined();
		orLabel.addStyleName(Reindeer.LABEL_SMALL);

		return orLabel;
	}

	protected DragAndDropWrapper buildDropFileComponent() {
		Panel dropPanel = new Panel();

		Label dropLabel = new Label(Messages.getValue(GoesConstants.MESSAGE_DROP_FILE));
		dropLabel.setSizeUndefined();
		dropPanel.addComponent(dropLabel);
		((VerticalLayout) dropPanel.getContent()).setComponentAlignment(dropLabel, Alignment.MIDDLE_CENTER);

		dropFileComponent = new DragAndDropWrapper(dropPanel);
		dropFileComponent.setDropHandler(this);
		dropFileComponent.setWidth("80%");

		return dropFileComponent;
	}

	private VerticalLayout buildUploadLayout() {
		uploadLayout = new VerticalLayout();
		uploadLayout.setSizeFull();

		pleaseWaitLabel = buildPleaseWaitLabel();
		uploadLayout.addComponent(pleaseWaitLabel);
		uploadLayout.setComponentAlignment(pleaseWaitLabel, Alignment.MIDDLE_CENTER);

		descriptionLabel = buildDescriptionLabel();
		uploadLayout.addComponent(descriptionLabel);
		uploadLayout.setComponentAlignment(descriptionLabel, Alignment.MIDDLE_CENTER);

		progressLayout = buildProgressLayout();
		uploadLayout.addComponent(progressLayout);
		uploadLayout.setComponentAlignment(progressLayout, Alignment.MIDDLE_CENTER);

		return uploadLayout;
	}

	private Label buildPleaseWaitLabel() {
		pleaseWaitLabel = new Label(Messages.getValue(GoesConstants.MESSAGE_WAIT));
		pleaseWaitLabel.setVisible(false);
		return pleaseWaitLabel;
	}

	private Label buildDescriptionLabel() {
		descriptionLabel = new Label();
		descriptionLabel.addStyleName(Reindeer.LABEL_SMALL);
		return descriptionLabel;
	}

	private HorizontalLayout buildProgressLayout() {
		progressLayout = new HorizontalLayout();
		progressLayout.setSpacing(true);
		progressLayout.setSizeUndefined();

		progressIndicator = buildProgressIndicator();
		progressLayout.addComponent(progressIndicator);

		cancelButton = buildCancelButton();
		progressLayout.addComponent(cancelButton);

		return progressLayout;
	}

	private ProgressIndicator buildProgressIndicator() {
		progressIndicator = new ProgressIndicator();
		return progressIndicator;
	}

	private Button buildCancelButton() {
		cancelButton = new Button(GoesConstants.BUTTON_CANCEL);
		cancelButton.addListener((ClickListener) this);
		cancelButton.setStyleName(Reindeer.BUTTON_SMALL);
		return cancelButton;
	}

	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {
		try {
			String tomcatHome = System.getenv("CATALINA_HOME");
			String javaTempFolder = System.getProperty("java.io.tmpdir");
			String filePath;
			if (tomcatHome != null)
				filePath = tomcatHome.concat("\\temp\\").concat(filename);
			else
				filePath = javaTempFolder.concat("\\").concat(filename);
			System.out.println("======> File Path " + filePath);
			file = new File(filePath);
			file.getParentFile().mkdirs();
			file.createNewFile();
			return new FileOutputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void uploadStarted(StartedEvent event) {
		// dont start the upload if file type doesnt match
		String mimeType = event.getMIMEType();
		if (!acceptedMimeTypes.contains(mimeType) || (event.getFilename() == null)) {
			upload.interruptUpload();
			AppContext.getMainWindow().showNotification("Error", Messages.getValue(GoesConstants.MESSAGE_FILE_MISMATCH), Notification.TYPE_ERROR_MESSAGE);
			return;
		}
		descriptionLabel.setValue(event.getFilename());
		progressIndicator.setValue(0f);
		progressIndicator.setPollingInterval(50);
		uploadMode();
	}

	@Override
	public void uploadFinished(FinishedEvent event) {
		startMode();
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		for (SucceededListener succeededListener : succeededListeners) {
			succeededListener.uploadSucceeded(event);
		}
	}

	@Override
	public void uploadFailed(Upload.FailedEvent event) {
		if (event != null)
			AppContext.getMainWindow()
					.showNotification(Messages.getValue(GoesConstants.MESSAGE_INTERRUPT, event.getFilename()), Window.Notification.TYPE_WARNING_MESSAGE);
		startMode();
		for (FailedListener failedListener : failedListeners) {
			failedListener.uploadFailed(event);
		}
	}

	@Override
	public void updateProgress(long readBytes, long contentLength) {
		progressIndicator.setValue(new Float(readBytes / (float) contentLength));
	}

	// Drag and drop handling (DropHandler)
	@Override
	public void drop(DragAndDropEvent event) {
		WrapperTransferable transferable = (WrapperTransferable) event.getTransferable();
		Html5File[] files = transferable.getFiles();
		if (files == null)
			return;
		if (files.length == 1) {
			// only support for one file upload at this moment
			final Html5File file = files[0];
			file.setStreamVariable(new StreamVariable() {
				@Override
				public void streamingStarted(StreamingStartEvent event) {
					uploadStarted(new StartedEvent(upload, file.getFileName(), file.getType(), file.getFileSize()));
				}

				@Override
				public void streamingFinished(StreamingEndEvent event) {
					uploadFinished(null); // event doesnt matter here
					uploadSucceeded(new SucceededEvent(upload, file.getFileName(), file.getType(), file.getFileSize()));
				}

				@Override
				public void streamingFailed(StreamingErrorEvent event) {
					uploadFailed(null);
				}

				@Override
				public void onProgress(StreamingProgressEvent event) {
					updateProgress(event.getBytesReceived(), event.getContentLength());
				}

				@Override
				public boolean listenProgress() {
					return true;
				}

				@Override
				public boolean isInterrupted() {
					return false;
				}

				@Override
				public OutputStream getOutputStream() {
					return receiveUpload(file.getFileName(), file.getType());
				}
			});
		}
	}

	@Override
	public AcceptCriterion getAcceptCriterion() {
		return AcceptAll.get();
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button button = event.getButton();
		if (button == cancelButton)
			upload.interruptUpload();
	}

	public void startMode() {
		removeAllComponents();
		addComponent(startLayout);
	}

	public void uploadMode() {
		removeAllComponents();
		addComponent(uploadLayout);
	}

	public File getFile() {
		return file;
	}

	public List<SucceededListener> getSucceededListeners() {
		return succeededListeners;
	}

	public void addSucceededListeners(List<SucceededListener> succeededListeners) {
		this.succeededListeners = succeededListeners;
	}

	public List<FailedListener> getFailedListeners() {
		return failedListeners;
	}

	public void setFailedListeners(List<FailedListener> failedListeners) {
		this.failedListeners = failedListeners;
	}

	public List<String> getAcceptedMimeTypes() {
		return acceptedMimeTypes;
	}

	public void setAcceptedMimeTypes(List<String> acceptedMimeTypes) {
		this.acceptedMimeTypes = acceptedMimeTypes;
	}

	@Override
	public void focus() {
		upload.focus();
	}
}