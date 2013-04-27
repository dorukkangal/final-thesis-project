package tr.edu.gsu.view.confirmation;

import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.GoesConstants;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class ConfirmationDialogPopupWindow extends PopupWindow {

	private GridLayout layout;
	private Label descriptionLabel;
	private Button yesButton;
	private Button noButton;

	public ConfirmationDialogPopupWindow(String description) {
		this(null, description);
	}

	public ConfirmationDialogPopupWindow(String title, String description) {
		initialize(title, description);
	}

	private void initialize(String title, String description) {
		setWidth(400, UNITS_PIXELS);
		setModal(true);
		setResizable(false);

		addStyleName(Reindeer.PANEL_LIGHT);

		layout = new GridLayout(3, 2);
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		setContent(layout);
		if (title != null) {
			setCaption(title);
		} else {
			setCaption(Messages.getValue(GoesConstants.MESSAGE_CONFIRMATION_DIALOG));
		}

		initLabel(description);
		initButtons();
	}

	private void initLabel(String description) {
		descriptionLabel = new Label(description, Label.CONTENT_XHTML);
		descriptionLabel.setSizeFull();
		layout.addComponent(descriptionLabel, 0, 0, 2, 0);
		layout.setRowExpandRatio(0, 1.0f);
	}

	private void initButtons() {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		layout.addComponent(buttonLayout, 2, 1);

		noButton = new Button(Messages.getValue(GoesConstants.BUTTON_NO));
		noButton.setClickShortcut(KeyCode.ESCAPE, null);
		buttonLayout.addComponent(noButton);
		noButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				close();
				fireEvent(new ConfirmationEvent(ConfirmationDialogPopupWindow.this, false));
			}
		});

		yesButton = new Button(Messages.getValue(GoesConstants.BUTTON_YES));
		yesButton.setDisableOnClick(true);
		yesButton.focus();
		buttonLayout.addComponent(yesButton);
		yesButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				close();
				fireEvent(new ConfirmationEvent(ConfirmationDialogPopupWindow.this, true));
			}
		});
	}
}
