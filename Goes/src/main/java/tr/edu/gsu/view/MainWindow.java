package tr.edu.gsu.view;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.domain.user.GoesUser;
import tr.edu.gsu.domain.user.Student;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.session.GoesSession;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.util.ImageUtil;
import tr.edu.gsu.view.page.BasePage;
import tr.edu.gsu.view.page.exam.ExamForm;
import tr.edu.gsu.view.page.exam.ExamView;
import tr.edu.gsu.view.page.login.LoginForm;
import tr.edu.gsu.view.page.message.MessageView;
import tr.edu.gsu.view.page.profile.ProfileView;
import tr.edu.gsu.view.page.question.QuestionForm;
import tr.edu.gsu.view.page.question.QuestionView;
import tr.edu.gsu.view.page.student.StudentDetailView;
import tr.edu.gsu.view.page.student.StudentView;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

public class MainWindow extends Window implements LayoutClickListener, ClickListener {
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;
	private HorizontalLayout topLayout;
	private HorizontalLayout bottomLayout;
	private VerticalLayout rightLayout;

	private Embedded logo;
	private HorizontalLayout infoLayout;
	private Embedded avatarContainer;
	private Label infoLabel;
	private HorizontalLayout languageLayout;
	private Button languageTurkish;
	private Button languageFrench;

	private LeftMenu leftMenu;
	private LeftMenu.Tab examTab;
	private LeftMenu.Tab questionTab;
	private LeftMenu.Tab studentTab;
	private LeftMenu.Tab profileTab;
//	private LeftMenu.Tab messageTab;
	private LeftMenu.Tab logoutTab;

	private GoesSession session;

	@Override
	public void attach() {
		mainLayout = buildMainLayout();
		setContent(mainLayout);
		getContent().setSizeFull();
		showExamView();
		leftMenu.setSelected(examTab);

		setSizeFull();
		super.attach();
	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		topLayout = buildTopLayout();
		Panel topContainer = new Panel();
		topContainer.setScrollable(false);
		topContainer.setSizeFull();
		topContainer.setContent(topLayout);
		mainLayout.addComponent(topContainer);
		mainLayout.setExpandRatio(topContainer, 1.0f);

		bottomLayout = buildBottomLayout();
		mainLayout.addComponent(bottomLayout);
		mainLayout.setExpandRatio(bottomLayout, 7.0f);

		return mainLayout;
	}

	private HorizontalLayout buildTopLayout() {
		topLayout = new HorizontalLayout();
		topLayout.setSizeFull();

		logo = buildLogo();
		topLayout.addComponent(logo);
		topLayout.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);

		infoLayout = buildInfoLayout();
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.addComponent(infoLayout);
		hl.setComponentAlignment(infoLayout, Alignment.MIDDLE_CENTER);
		topLayout.addComponent(hl);
		topLayout.setExpandRatio(hl, 1.0f);

		languageLayout = buildLanguageLayout();
		topLayout.addComponent(languageLayout);
		topLayout.setComponentAlignment(languageLayout, Alignment.MIDDLE_RIGHT);

		return topLayout;
	}

	private Embedded buildLogo() {
		logo = new Embedded();
		logo.setSource(new ThemeResource(GoesConstants.ICON_LOGO));
//		logo.setHeight("100%");

		return logo;
	}

	private HorizontalLayout buildInfoLayout() {
		infoLayout = new HorizontalLayout();
		infoLayout.setSizeUndefined();
		infoLayout.setSpacing(true);

		avatarContainer = new Embedded();
		infoLayout.addComponent(avatarContainer);

		infoLabel = new Label();
		infoLabel.setContentMode(Label.CONTENT_XHTML);
		infoLabel.setSizeUndefined();
		infoLayout.addComponent(infoLabel);
		setInfoMessage();

		return infoLayout;
	}

	private HorizontalLayout buildLanguageLayout() {
		languageLayout = new HorizontalLayout();
		languageLayout.setMargin(true);

		languageTurkish = new Button(Messages.getValue(GoesConstants.BUTTON_TR));
		languageTurkish.setStyleName(BaseTheme.BUTTON_LINK);
		languageTurkish.addListener(this);

		languageFrench = new Button();
		languageFrench.setCaption(Messages.getValue(GoesConstants.BUTTON_FR));
		languageFrench.setStyleName(BaseTheme.BUTTON_LINK);
		languageFrench.addListener(this);

		languageLayout.addComponent(languageTurkish);
		languageLayout.addComponent(new Embedded(null, new ThemeResource(GoesConstants.ICON_SEPERATOR)));
		languageLayout.addComponent(languageFrench);

		if (session.getLocale() == GoesConstants.LOCALE_TURKISH) {
			languageTurkish.addStyleName(GoesConstants.STYLE_SELECTED_LANGUAGE);
			languageFrench.removeStyleName(GoesConstants.STYLE_SELECTED_LANGUAGE);
			languageTurkish.setReadOnly(true);
			languageFrench.setReadOnly(false);
		} else {
			languageFrench.addStyleName(GoesConstants.STYLE_SELECTED_LANGUAGE);
			languageTurkish.removeStyleName(GoesConstants.STYLE_SELECTED_LANGUAGE);
			languageFrench.setReadOnly(true);
			languageTurkish.setReadOnly(false);
		}
		return languageLayout;
	}

	private HorizontalLayout buildBottomLayout() {
		bottomLayout = new HorizontalLayout();
		bottomLayout.setSizeFull();

		leftMenu = buildLeftMenu();
		bottomLayout.addComponent(leftMenu);
		bottomLayout.setExpandRatio(leftMenu, 1.0f);

		rightLayout = buildRightLayout();
		bottomLayout.addComponent(rightLayout);
		bottomLayout.setExpandRatio(rightLayout, 7.0f);

		return bottomLayout;
	}

	private LeftMenu buildLeftMenu() {
		leftMenu.removeAllComponents();
		leftMenu.setEnabled(true);

		examTab = leftMenu.addTab(Messages.getValue(GoesConstants.MENU_EXAM), null, true);
		examTab.addListener((LayoutClickListener) this);
		examTab.getNewButton().addListener(this);

		questionTab = leftMenu.addTab(Messages.getValue(GoesConstants.MENU_QUESTION), null, true);
		questionTab.addListener((LayoutClickListener) this);
		questionTab.getNewButton().addListener(this);

		studentTab = leftMenu.addTab(Messages.getValue(GoesConstants.MENU_STUDENT), null, false);
		studentTab.addListener((LayoutClickListener) this);

		leftMenu.addSpacer();

		profileTab = leftMenu.addTab(Messages.getValue(GoesConstants.MENU_PROFILE), null, false);
		profileTab.addListener((LayoutClickListener) this);

		//not implemented
//		messageTab = leftMenu.addTab(Messages.getValue(GoesConstants.MENU_MESSAGE), null, false);
//		messageTab.addListener((LayoutClickListener) this);

		leftMenu.addSpacer();

		logoutTab = leftMenu.addTab(Messages.getValue(GoesConstants.MENU_LOGOUT), null, false);
		logoutTab.addListener((LayoutClickListener) this);

		return leftMenu;
	}

	private VerticalLayout buildRightLayout() {
		rightLayout = new VerticalLayout();
		rightLayout.setSizeFull();

		return rightLayout;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();
		if (source == languageTurkish) {
			Messages.initBundle(GoesConstants.LOCALE_TURKISH);
			attach();
		} else if (source == languageFrench) {
			Messages.initBundle(GoesConstants.LOCALE_FRENCH);
			attach();
		}
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
		Component c = event.getClickedComponent();
		if (c instanceof Label) {
			Label source = (Label) c;
			if (source == examTab.getLabel())
				showExamView();
			else if (source == questionTab.getLabel())
				showQuestionView();
			else if (source == studentTab.getLabel())
				showStudentView();
			else if (source == profileTab.getLabel())
				showProfileView();
//			else if (source == messageTab.getLabel())
//				showMessageView();
			else if (source == logoutTab.getLabel())
				logout();
		} else if (c instanceof Button) {
			Button source = (Button) c;
			if (source == examTab.getNewButton())
				showExamForm(null);
			else if (source == questionTab.getNewButton())
				showQuestionForm(true, null);
		}
	}

	public void showExamView() {
		show(new ExamView());
	}

	public void showExamForm(Exam exam) {
		show(new ExamForm(exam));
	}

	public void showQuestionView() {
		show(new QuestionView());
	}

	public void showQuestionForm(boolean editMode, Question question) {
		show(new QuestionForm(editMode, question));
	}

	public void showStudentView() {
		show(new StudentView());
	}

	public void showStudentDetailView(Student student) {
		show(new StudentDetailView(student));
	}

	public void showProfileView() {
		Teacher currentTeacher = (Teacher) session.getUser();
		show(new ProfileView(currentTeacher));
	}

	public void showMessageView() {
		show(new MessageView());
	}

	public void showMessageForm() {
		show(new MessageView());
	}

	private void show(BasePage view) {
		if (session.getUser() != null) {
			rightLayout.removeAllComponents();
			rightLayout.addComponent(view);
			rightLayout.setExpandRatio(view, 1.0f);
		} else {
			LoginForm loginForm = new LoginForm();
			rightLayout.removeAllComponents();
			rightLayout.addComponent(loginForm);
			rightLayout.setExpandRatio(loginForm, 1.0f);

			leftMenu.setEnabled(false);
		}
	}

	public void logout() {
		session.setUser(null);
		// show(new LoginForm());
		attach();
		// AppContext.getApp().close();
		// AppContext.getApp().init();
	}

	public void setInfoMessage() {
		GoesUser user = session.getUser();
		if (user != null) {
			avatarContainer.setSource(ImageUtil.resizeImage(user.getPicture(), 40, true));
			String message = Messages.getValue(GoesConstants.MESSAGE_WELCOME, user.getFullName(), user.getDepartment().getName());
			infoLabel.setValue(message);
		}
	}

	public void setLeftMenu(LeftMenu leftMenu) {
		this.leftMenu = leftMenu;
	}

	public void setSession(GoesSession session) {
		this.session = session;
	}
}
