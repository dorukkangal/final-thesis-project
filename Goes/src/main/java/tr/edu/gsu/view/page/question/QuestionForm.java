package tr.edu.gsu.view.page.question;

import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.thomas.timefield.TimeField;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.domain.exam.QuestionField;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.service.exam.QuestionService;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.confirmation.ConfirmationDialogPopupWindow;
import tr.edu.gsu.view.confirmation.ConfirmationEvent;
import tr.edu.gsu.view.confirmation.ConfirmationEventListener;
import tr.edu.gsu.view.confirmation.PopupWindow;
import tr.edu.gsu.view.decoration.BlueBar;
import tr.edu.gsu.view.page.BasePage;
import tr.edu.gsu.view.page.question.component.QuestionChoice;
import tr.edu.gsu.view.page.question.component.QuestionImage;
import tr.edu.gsu.view.page.question.component.QuestionSound;
import tr.edu.gsu.view.page.question.component.QuestionText;
import tr.edu.gsu.view.page.question.component.QuestionVideo;
import tr.edu.gsu.view.toolbar.ToolBar;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import com.velociti.ikarus.ui.widget.IkarusNumericField;

import fi.jasoft.dragdroplayouts.DDAbsoluteLayout;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.drophandlers.DefaultAbsoluteLayoutDropHandler;

@SuppressWarnings("serial")
public class QuestionForm extends BasePage implements LayoutClickListener, ToolBar.ToolbarCommand {

	private ToolBar.Entry editEntry;
	private ToolBar.Entry saveEntry;
	private ToolBar.Entry deleteEntry;
	private ToolBar.Entry cancelEntry;

	private GridLayout formLayout;
	private TextField nameField;
	private TimeField timeField;
	private IkarusNumericField pointField;
	private Label errorLabel;

	private Panel container;
	private AbsoluteLayout absoluteLayout;

	private QuestionService questionService;
	private Question question;
	private boolean editMode = false;

	public QuestionForm(boolean editMode, Question question) {
		this.editMode = editMode;
		this.question = (question != null) ? question : new Question();
		this.questionService = AppContext.getQuestionService();
	}

	@Override
	public ToolBar buildToolBar() {
		toolBar = new ToolBar();
		if (editMode) {
			buildEditedModeToolBar();
		} else {
			buildPreviewModeToolBar();
		}
		return toolBar;
	}

	private void buildEditedModeToolBar() {
		saveEntry = toolBar.addToolbarEntry(GoesConstants.BUTTON_SAVE, Messages.getValue(GoesConstants.BUTTON_SAVE), this);
		deleteEntry = toolBar.addToolbarEntry(GoesConstants.BUTTON_DELETE, Messages.getValue(GoesConstants.BUTTON_DELETE), this);
		cancelEntry = toolBar.addToolbarEntry(GoesConstants.BUTTON_CANCEL, Messages.getValue(GoesConstants.BUTTON_CANCEL), this);
	}

	private void buildPreviewModeToolBar() {
		editEntry = toolBar.addToolbarEntry(GoesConstants.BUTTON_EDIT, Messages.getValue(GoesConstants.BUTTON_EDIT), this);
	}

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		mainLayout.addComponent(new BlueBar(Messages.getValue(GoesConstants.QUESTION_INFORMATION)));

		formLayout = buildFormLayout();
		mainLayout.addComponent(formLayout);

		mainLayout.addComponent(new BlueBar(Messages.getValue(GoesConstants.QUESTION_CONTENT)));

		container = buildContainer();
		mainLayout.addComponent(container);
		mainLayout.setExpandRatio(container, 1.0f);

		return mainLayout;
	}

	private GridLayout buildFormLayout() {
		formLayout = new GridLayout(3, 2);
		formLayout.setSpacing(true);
		formLayout.setWidth("600px");
		formLayout.setMargin(true);

		nameField = buildNameField();
		formLayout.addComponent(nameField, 0, 0);

		timeField = buildTimeField();
		formLayout.addComponent(timeField, 1, 0);

		pointField = buildPointField();
		formLayout.addComponent(pointField, 2, 0);

		errorLabel = buildErrorLabel();
		formLayout.addComponent(errorLabel, 0, 1, 2, 1);

		return formLayout;
	}

	private TextField buildNameField() {
		nameField = new TextField();
		nameField.setCaption(Messages.getValue(GoesConstants.QUESTION_NAME));
		nameField.setImmediate(true);
		nameField.setRequired(true);
		nameField.setNullRepresentation("");

		return nameField;
	}

	private TimeField buildTimeField() {
		timeField = new TimeField();
		timeField.setCaption(Messages.getValue(GoesConstants.QUESTION_TIME));
		timeField.setImmediate(true);
		timeField.setRequired(true);
		timeField.set24HourClock(true);

		return timeField;
	}

	private IkarusNumericField buildPointField() {
		pointField = new IkarusNumericField();
		pointField.setCaption(Messages.getValue(GoesConstants.QUESTION_POINT));
		pointField.setImmediate(true);
		pointField.setRequired(true);
		pointField.setNullRepresentation("0");
		pointField.setScaleSize(0);
		pointField.setGroupingSeperator("");
		pointField.setTextAlignment(IkarusNumericField.LEFT);
		pointField.setMaxLength(4);
		pointField.setColumns(4);

		return pointField;
	}

	private Label buildErrorLabel() {
		errorLabel = new Label("", Label.CONTENT_XHTML);
		errorLabel.setImmediate(true);

		return errorLabel;
	}

	private Panel buildContainer() {
		container = new Panel();
		container.setSizeFull();
		container.setScrollable(true);
		container.getContent().setWidth("100%");

		if (editMode)
			absoluteLayout = buildEditLayout();
		else
			absoluteLayout = buildPreviewLayout();
		absoluteLayout.setWidth("100%");
		absoluteLayout.setHeight("800px");
		container.getContent().addComponent(absoluteLayout);

		return container;
	}

	private AbsoluteLayout buildEditLayout() {
		absoluteLayout = new DDAbsoluteLayout();
		absoluteLayout.setSizeFull();
		absoluteLayout.addStyleName(GoesConstants.STYLE_DRAGGABLE_LAYOUT);
		((DDAbsoluteLayout) absoluteLayout).setDragMode(LayoutDragMode.CLONE);
		((DDAbsoluteLayout) absoluteLayout).setDropHandler(new DefaultAbsoluteLayoutDropHandler());
		absoluteLayout.addListener((LayoutEvents.LayoutClickListener) this);

		return absoluteLayout;
	}

	private AbsoluteLayout buildPreviewLayout() {
		absoluteLayout = new AbsoluteLayout();
		absoluteLayout.setSizeFull();

		return absoluteLayout;
	}

	@Override
	public void refreshContent() {
		nameField.setValue(question.getName());
		nameField.setReadOnly(!editMode);
		timeField.setValue(question.getTime() != null ? question.getTime() : new Date());
		timeField.setReadOnly(!editMode);
		for (Iterator<Component> itr = timeField.getComponentIterator(); itr.hasNext();) {
			HorizontalLayout c = (HorizontalLayout) itr.next();
			c.setReadOnly(!editMode);
			for (Iterator<Component> itr2 = c.getComponentIterator(); itr2.hasNext();)
				itr2.next().setReadOnly(!editMode);
		}
		pointField.setValue(String.valueOf(question.getPoint()));
		pointField.setReadOnly(!editMode);
		populateComponents();
	}

	private void populateComponents() {
		Set<QuestionField> fields = question.getFields();
		for (QuestionField field : fields) {
			QuestionComponent component = null;
			if (field.getType().equals(GoesConstants.QUESTION_TEXT)) {
				component = new QuestionText(editMode);
				absoluteLayout.addComponent((QuestionText) component, getCoordinates(field.getDistanceFromTop(), field.getDistanceFromLeft()));
			} else if (field.getType().equals(GoesConstants.QUESTION_CHOICE)) {
				component = new QuestionChoice(editMode);
				component.setTrue(field.isTrue());
				absoluteLayout.addComponent((QuestionChoice) component, getCoordinates(field.getDistanceFromTop(), field.getDistanceFromLeft()));
			} else if (field.getType().equals(GoesConstants.QUESTION_IMAGE)) {
				component = new QuestionImage();
				absoluteLayout.addComponent((QuestionImage) component, getCoordinates(field.getDistanceFromTop(), field.getDistanceFromLeft()));
			} else if (field.getType().equals(GoesConstants.QUESTION_SOUND)) {
				component = new QuestionSound();
				absoluteLayout.addComponent((QuestionSound) component, getCoordinates(field.getDistanceFromTop(), field.getDistanceFromLeft()));
			} else if (field.getType().equals(GoesConstants.QUESTION_VIDEO)) {
				component = new QuestionVideo();
				absoluteLayout.addComponent((QuestionVideo) component, getCoordinates(field.getDistanceFromTop(), field.getDistanceFromLeft()));
			}
			component.setContent(field.getContent());
			component.resize(Math.round(Float.valueOf(field.getWidth())), Math.round(Float.valueOf(field.getHeight())));
		}
	}

	@Override
	public void toolBarItemSelected(ToolBar.Entry toolbarEntry) {
		if (toolbarEntry == editEntry) {
			editMode = true;
			init();
			refreshContent();
		} else if (toolbarEntry == saveEntry) {
			if (isValid()) {
				if (question.getCreator() == null)
					question.setCreator((Teacher) session.getUser());
				questionService.update(question);
				mainWindow.showQuestionView();
				mainWindow.showNotification(Messages.getValue(GoesConstants.MESSAGE_SUCCESFULLY_SAVED));
			}
		} else if (toolbarEntry == deleteEntry) {
			deleteQuestionIfPossible();
		} else if (toolbarEntry == cancelEntry) {
			ConfirmationDialogPopupWindow popupWindow = new ConfirmationDialogPopupWindow(Messages.getValue(GoesConstants.MESSAGE_DISCARD_CHANGES));
			popupWindow.addListener(new ConfirmationEventListener() {
				@Override
				protected void confirmed(ConfirmationEvent event) {
					mainWindow.showQuestionView();
				}

				@Override
				protected void rejected(ConfirmationEvent event) {
				}
			});
			mainWindow.addWindow(popupWindow);
		}
	}

	private boolean isValid() {
		String name = (String) nameField.getValue();
		if (name != null && !name.equals(""))
			question.setName(name);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.QUESTION_NAME)));
			return false;
		}

		Time time = new Time(((Date) timeField.getValue()).getTime());
		if (time != null && !time.toString().equals("00:00:00"))
			question.setTime(time);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.QUESTION_TIME)));
			return false;
		}

		int point = Integer.valueOf((String) pointField.getValue());
		if (point != 0)
			question.setPoint(point);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.QUESTION_POINT)));
			return false;
		}

		question.getFields().clear();
		Set<QuestionField> fields = collectFields();
		if (fields != null && !fields.isEmpty())
			question.getFields().addAll(fields);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.QUESTION_FIELDS)));
			return false;
		}
		return true;
	}

	private Set<QuestionField> collectFields() {
		Set<QuestionField> fields = new HashSet<>();
		for (Iterator<Component> itr = absoluteLayout.getComponentIterator(); itr.hasNext();) {
			Component c = itr.next();
			if (c instanceof QuestionComponent) {
				QuestionComponent questionComponent = (QuestionComponent) c;
				ComponentPosition pos = absoluteLayout.getPosition(c);
				String top = pos.getTopValue().toString();
				String left = pos.getLeftValue().toString();
				String width = Float.toString(questionComponent.getWidth());
				String height = Float.toString(questionComponent.getHeight());
				byte[] content = questionComponent.getContent();
				if (content != null) {
					QuestionField field = new QuestionField(questionComponent.getType(), content, questionComponent.isTrue(), questionComponent.getMimeType(), top, left,
							width, height);
					fields.add(field);
				} else {
					return null;
				}
			}
		}
		return fields;
	}

	private void deleteQuestionIfPossible() {
		String message = "";
		List<Exam> exams = AppContext.getExamService().findAll();
		for (Exam exam : exams) {
			for (Question question : exam.getQuestions()) {
				if (question.getId() == this.question.getId()) {
					message += exam.getName() + ", ";
				}
			}
		}
		if (message.equals("")) {
			questionService.delete(question);
			mainWindow.showQuestionView();
			mainWindow.showNotification(Messages.getValue(GoesConstants.MESSAGE_SUCCESFULLY_DELETED));
		} else {
			mainWindow.showNotification(Messages.getValue(GoesConstants.QUESTION_DELETION_ERROR, message), Notification.TYPE_WARNING_MESSAGE);
		}
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
		Component c = event.getClickedComponent();
		if (event.getButton() == MouseEvents.ClickEvent.BUTTON_RIGHT) {
			if (c instanceof DDAbsoluteLayout || c == null) {
				RightClickMenu layoutClickMenu = new RightClickMenu(absoluteLayout, event.getRelativeX(), event.getRelativeY());
				absoluteLayout.addComponent(layoutClickMenu);
				layoutClickMenu.show(event.getClientX(), event.getClientY());
			} else {
				c = getParentComponent(c);
				if (c instanceof QuestionComponent || c.getParent().equals(absoluteLayout)) {
					RightClickMenu layoutClickMenu = new RightClickMenu(c, event.getClientX(), event.getClientY());
					absoluteLayout.addComponent(layoutClickMenu);
					layoutClickMenu.show(event.getClientX(), event.getClientY());
				}
			}
		} else if (event.getButton() == MouseEvents.ClickEvent.BUTTON_LEFT) {
			if (!(c instanceof DDAbsoluteLayout) && c != null) {
				c = getParentComponent(c);
				if (c instanceof QuestionComponent || c.getParent().equals(absoluteLayout)) {
					((QuestionComponent) c).focus();
				}
			}
		}
	}

	private Component getParentComponent(Component c) {
		while ((c != null) && !(c instanceof QuestionComponent))
			c = c.getParent();
		return c;
	}

	public class RightClickMenu extends ContextMenu implements ContextMenu.ClickListener {
		private static final long serialVersionUID = 1L;

		private final String ADD_TEXT = Messages.getValue(GoesConstants.ACTION_ADD_TEXT);
		private final String ADD_ANSWER = Messages.getValue(GoesConstants.ACTION_ADD_ANSWER);
		private final String ADD_IMAGE = Messages.getValue(GoesConstants.ACTION_ADD_IMAGE);
		private final String ADD_SOUND = Messages.getValue(GoesConstants.ACTION_ADD_SOUND);
		private final String ADD_VIDEO = Messages.getValue(GoesConstants.ACTION_ADD_VIDEO);

		private final String REMOVE = Messages.getValue(GoesConstants.ACTION_REMOVE);
		private final String RESIZE = Messages.getValue(GoesConstants.ACTION_RESIZE);

		private Component c;
		private Integer top;
		private Integer left;

		private Window resizeWindow;
		private Button resizeSubmit;
		private TextField widthField;
		private TextField heightField;

		public RightClickMenu(Component c, int left, int top) {
			super();
			this.c = c;
			this.left = left;
			this.top = top;
			if (c instanceof AbsoluteLayout)
				buildLayoutClickMenu();
			if (c instanceof QuestionComponent)
				buildFieldClickMenu();
		}

		public void buildLayoutClickMenu() {
			this.setWidth("180px");
			this.setHeight("150px");
			this.addListener(this);

			this.addItem(ADD_TEXT);
			this.addItem(ADD_ANSWER);
			this.addItem(ADD_IMAGE);
			this.addItem(ADD_SOUND);
			this.addItem(ADD_VIDEO);
		}

		public void buildFieldClickMenu() {
			this.setWidth("180.0px");
			this.setHeight("150.0px");
			this.addListener(this);

			if (((QuestionComponent) c).isResizeable())
				this.addItem(RESIZE);
			this.addItem(REMOVE);
		}

		@Override
		public void contextItemClick(ClickEvent event) {
			ContextMenuItem menuItem = event.getClickedItem();
			if (c instanceof AbsoluteLayout)
				clickLayout(menuItem);
			if (c instanceof QuestionComponent)
				clickField(menuItem);
		}

		private void clickLayout(ContextMenuItem menuItem) {
			AbsoluteLayout layout = (AbsoluteLayout) c;
			if (menuItem.getName().equals(ADD_TEXT)) {
				layout.addComponent(new QuestionText(editMode), getCoordinates(top.toString(), left.toString()));
			} else if (menuItem.getName().equals(ADD_ANSWER)) {
				layout.addComponent(new QuestionChoice(editMode), getCoordinates(top.toString(), left.toString()));
			} else if (menuItem.getName().equals(ADD_IMAGE)) {
				layout.addComponent(new QuestionImage(), getCoordinates(top.toString(), left.toString()));
			} else if (menuItem.getName().equals(ADD_SOUND)) {
				layout.addComponent(new QuestionSound(), getCoordinates(top.toString(), left.toString()));
			} else if (menuItem.getName().equals(ADD_VIDEO)) {
				layout.addComponent(new QuestionVideo(), getCoordinates(top.toString(), left.toString()));
			}
		}

		private void clickField(ContextMenuItem menuItem) {
			QuestionComponent field = (QuestionComponent) c;
			if (menuItem.getName().equals(RESIZE)) {
				showResizeWindow(field);
			} else if (menuItem.getName().equals(REMOVE)) {
				absoluteLayout.removeComponent(c);
			}
		}

		private void showResizeWindow(final QuestionComponent field) {
			resizeWindow = new PopupWindow();
			resizeWindow.setStyleName(Reindeer.WINDOW_LIGHT);
			resizeWindow.center();
			resizeWindow.setCaption(Messages.getValue(GoesConstants.ACTION_RESIZE));
			resizeWindow.getContent().setSizeUndefined();

			VerticalLayout vl = new VerticalLayout();
			vl.setSpacing(true);
			resizeWindow.addComponent(vl);

			HorizontalLayout hl = new HorizontalLayout();
			hl.setSpacing(true);
			vl.addComponent(hl);

			widthField = buildWidthField(field);
			hl.addComponent(widthField);
			hl.addComponent(new Label("px"));

			heightField = buildHeightField(field);
			hl.addComponent(heightField);
			hl.addComponent(new Label("px"));

			resizeSubmit = new Button(Messages.getValue(GoesConstants.BUTTON_APPLY));
			resizeSubmit.addListener(new Button.ClickListener() {
				@Override
				public void buttonClick(Button.ClickEvent event) {
					String width = (String) widthField.getValue();
					String height = (String) heightField.getValue();
					field.resize(Integer.parseInt(width), Integer.parseInt(height));
					mainWindow.removeWindow(resizeWindow);
				}
			});
			vl.addComponent(resizeSubmit);
			vl.setComponentAlignment(resizeSubmit, Alignment.TOP_RIGHT);

			mainWindow.addWindow(resizeWindow);
		}

		private TextField buildWidthField(QuestionComponent field) {
			widthField = new TextField("Width");
			widthField.setStyleName(Reindeer.TEXTFIELD_SMALL);
			widthField.setValue(Integer.toString((int) field.getWidth()));
			widthField.addValidator(new RegexpValidator("-?\\d+", "Is not a number"));

			return widthField;
		}

		private TextField buildHeightField(QuestionComponent field) {
			heightField = new TextField("Height");
			heightField.setStyleName(Reindeer.TEXTFIELD_SMALL);
			heightField.setValue(Integer.toString((int) field.getHeight()));
			heightField.addValidator(new RegexpValidator("-?\\d+", "Is not a number"));
			return heightField;
		}

		public Component getComponent() {
			return c;
		}

		public void setComponent(Component c) {
			this.c = c;
		}

		public void setLeft(int left) {
			this.left = left;
		}

		public void setTop(int top) {
			this.top = top;
		}
	}

	public String getCoordinates(String top, String left) {
		return "top:" + top + "px; left:" + left + "px";
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	private String buildErrorMessage(String fieldName) {
		return "<span style='color:red'>" + Messages.getValue(GoesConstants.MESSAGE_IS_NOT_VALID, fieldName) + "</span>";
	}
}
