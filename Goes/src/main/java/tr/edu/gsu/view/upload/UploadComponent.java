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

public class UploadComponent extends VerticalLayout implements ClickListener, DropHandler, Receiver, StartedListener, FinishedListener, SucceededListener,
		FailedListener, ProgressListener {
	private static final long serialVersionUID = 1L;

	private Upload upload;
	private Label orLabel;
	private Label waitLabel;
	private Panel dropPanel;

	private HorizontalLayout progressLayout;
	private Label descriptionLabel;
	private ProgressIndicator progressIndicator;
	private Button cancelButton;

	private File file;

	private List<String> acceptedMimeTypes = new ArrayList<String>();

	private List<SucceededListener> succeededListeners = new ArrayList<SucceededListener>();
	private List<FailedListener> failedListeners = new ArrayList<FailedListener>();

	public UploadComponent(List<String> acceptedMimeTypes) {
		this.acceptedMimeTypes = acceptedMimeTypes;

		addUpload();
		addOrLabel();
		addDropPanel();
		addPleaseWaitLabel();
		addProgressLayout();
	}

	@Override
	public void attach() {
		super.attach();
		startedMode();
		setSizeFull();
	}

	// UI initialisation
	private void addUpload() {
		upload = new Upload();
		upload.setButtonCaption(Messages.getValue(GoesConstants.MESSAGE_SELECT));
		upload.setImmediate(true);

		upload.setReceiver((Upload.Receiver) this);
		upload.addListener((Upload.StartedListener) this);
		upload.addListener((Upload.ProgressListener) this);
		upload.addListener((Upload.FailedListener) this);
		upload.addListener((Upload.FinishedListener) this);
		upload.addListener((Upload.SucceededListener) this);

		addComponent(upload);
		setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
	}

	protected void addOrLabel() {
		orLabel = new Label(Messages.getValue(GoesConstants.MESSAGE_OR));
		orLabel.setSizeUndefined();
		orLabel.addStyleName(Reindeer.LABEL_SMALL);
		addComponent(orLabel);
		setComponentAlignment(orLabel, Alignment.MIDDLE_CENTER);
	}

	protected void addDropPanel() {
		dropPanel = new Panel();
		DragAndDropWrapper dragAndDropWrapper = new DragAndDropWrapper(dropPanel);
		dragAndDropWrapper.setDropHandler(this);
		dragAndDropWrapper.setWidth("80%");
		addComponent(dragAndDropWrapper);
		setComponentAlignment(dragAndDropWrapper, Alignment.MIDDLE_CENTER);

		Label dropLabel = new Label(Messages.getValue(GoesConstants.MESSAGE_DROP_FILE));
		dropLabel.setSizeUndefined();
		dropPanel.addComponent(dropLabel);
		((VerticalLayout) dropPanel.getContent()).setComponentAlignment(dropLabel, Alignment.MIDDLE_CENTER);
	}

	private void addPleaseWaitLabel() {
		waitLabel = new Label(Messages.getValue(GoesConstants.MESSAGE_WAIT));
		waitLabel.setVisible(false);
		addComponent(waitLabel);
	}

	private void addProgressLayout() {
		progressLayout = new HorizontalLayout();
		progressLayout.setSpacing(true);
		progressLayout.setSizeUndefined();
		addComponent(progressLayout);

		addDescription();
		addProgressIndicator();
		addCancelButton();
	}

	private void addDescription() {
		descriptionLabel = new Label();
		descriptionLabel.addStyleName(Reindeer.LABEL_SMALL);
		addComponent(descriptionLabel);
	}

	private void addProgressIndicator() {
		progressIndicator = new ProgressIndicator();
		progressLayout.addComponent(progressIndicator);
		progressLayout.setComponentAlignment(progressIndicator, Alignment.MIDDLE_LEFT);
	}

	private void addCancelButton() {
		cancelButton = new Button(GoesConstants.BUTTON_CANCEL);
		cancelButton.addListener((ClickListener) this);
		cancelButton.setStyleName(Reindeer.BUTTON_SMALL);
		progressLayout.addComponent(cancelButton);
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
			waitLabel.setVisible(false);
			return;
		}
		descriptionLabel.setValue(event.getFilename());
		progressIndicator.setValue(0f);
		progressIndicator.setPollingInterval(500);
		uploadMode();
	}

	@Override
	public void uploadFinished(FinishedEvent event) {
		startedMode();
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		for (SucceededListener succeededListener : succeededListeners) {
			succeededListener.uploadSucceeded(event);
		}
	}

	@Override
	public void uploadFailed(Upload.FailedEvent event) {
		AppContext.getMainWindow().showNotification(Messages.getValue(GoesConstants.MESSAGE_INTERRUPT, event.getFilename()), Window.Notification.TYPE_WARNING_MESSAGE);
		startedMode();
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
		waitLabel.setVisible(true);

		WrapperTransferable transferable = (WrapperTransferable) event.getTransferable();
		Html5File[] files = transferable.getFiles();
		if (files == null)
			return;
		if (files.length > 0) {
			// only support for one file upload at this moment
			final Html5File file = files[0];
			file.setStreamVariable(new StreamVariable() {
				private static final long serialVersionUID = 1L;

				public void streamingStarted(StreamingStartEvent event) {
					uploadStarted(new StartedEvent(upload, file.getFileName(), file.getType(), file.getFileSize()));
				}

				public void streamingFinished(StreamingEndEvent event) {
					uploadFinished(null); // event doesnt matter here
				}

				public void streamingFailed(StreamingErrorEvent event) {
					uploadFailed(null);
				}

				public void onProgress(StreamingProgressEvent event) {
					updateProgress(event.getBytesReceived(), event.getContentLength());
				}

				public boolean listenProgress() {
					return true;
				}

				public boolean isInterrupted() {
					return false;
				}

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

	public void startedMode() {
		upload.setVisible(true);
		progressLayout.setVisible(false);
	}

	public void uploadMode() {
		upload.setVisible(false);
		progressLayout.setVisible(true);
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