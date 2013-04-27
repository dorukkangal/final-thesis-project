package tr.edu.gsu.view.page.login;

import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.page.BasePage;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class LoginForm extends BasePage implements ClickListener {
	private static final long serialVersionUID = 1L;

	private Panel container;
	private FormLayout layout;
	private TextField usernameField;
	private PasswordField passwordField;
	private Label errorLabel;
	private Button loginButton;

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		container = buildContainer();
		mainLayout.addComponent(container);
		mainLayout.setComponentAlignment(container, Alignment.MIDDLE_CENTER);

		return mainLayout;
	}

	private Panel buildContainer() {
		container = new Panel(Messages.getValue(GoesConstants.LOGIN_FORM_CAPTION));
		// container.setWidth(40, UNITS_PERCENTAGE);
		// container.setHeight(40, UNITS_PERCENTAGE);
		container.setSizeUndefined();
		container.setScrollable(false);

		layout = buildLayout();
		container.setContent(layout);

		return container;
	}

	private FormLayout buildLayout() {
		layout = new FormLayout();
		layout.setSizeFull();
		layout.setMargin(true);

		usernameField = buildUsernameField();
		layout.addComponent(usernameField);

		passwordField = buildPasswordField();
		layout.addComponent(passwordField);

		errorLabel = buildErrorLabel();
		layout.addComponent(errorLabel);

		loginButton = buildLoginButton();
		layout.addComponent(loginButton);
		layout.setComponentAlignment(loginButton, Alignment.MIDDLE_RIGHT);

		return layout;
	}

	private TextField buildUsernameField() {
		usernameField = new TextField();
		usernameField.setCaption(Messages.getValue(GoesConstants.LOGIN_USERNAME));
		usernameField.setImmediate(true);
		usernameField.setRequired(true);
		usernameField.setNullRepresentation("");
		usernameField.focus();

		return usernameField;
	}

	private PasswordField buildPasswordField() {
		passwordField = new PasswordField();
		passwordField.setCaption(Messages.getValue(GoesConstants.LOGIN_PASSWORD));
		passwordField.setImmediate(true);
		passwordField.setRequired(true);
		passwordField.setNullRepresentation("");

		return passwordField;
	}

	private Label buildErrorLabel() {
		errorLabel = new Label("", Label.CONTENT_XHTML);
		errorLabel.setImmediate(true);

		return errorLabel;
	}

	private Button buildLoginButton() {
		loginButton = new Button();
		loginButton.setCaption(Messages.getValue(GoesConstants.LOGIN_BUTTON));
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addListener(this);

		return loginButton;
	}

	@Override
	public void refreshContent() {
		usernameField.setValue(null);
		passwordField.setValue(null);
		errorLabel.setValue("");
	}

	@Override
	public void buttonClick(ClickEvent event) {
		String username = (String) usernameField.getValue();
		String password = (String) passwordField.getValue();

		if ((username != null && !username.equals("")) && (password != null && !password.equals(""))) {
			Teacher teacher = AppContext.getTeacherService().find(username, password);
			if (teacher != null) {
				session.setUser(teacher);
				mainWindow.attach();
				return;
			}
		}
		refreshContent();
		errorLabel.setValue(Messages.getValue(GoesConstants.LOGIN_ERROR));
	}
}
