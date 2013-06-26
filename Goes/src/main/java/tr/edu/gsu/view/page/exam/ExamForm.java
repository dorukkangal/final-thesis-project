package tr.edu.gsu.view.page.exam;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import tr.edu.gsu.domain.exam.Course;
import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.service.exam.ExamService;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.confirmation.ConfirmationDialogPopupWindow;
import tr.edu.gsu.view.confirmation.ConfirmationEvent;
import tr.edu.gsu.view.confirmation.ConfirmationEventListener;
import tr.edu.gsu.view.page.BasePage;
import tr.edu.gsu.view.page.question.QuestionTable;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.terminal.gwt.client.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ExamForm extends BasePage implements ClickListener {

	private static final int TABLE_COUNT = 8;

	private GridLayout formLayout;
	private TextField nameField;
	private TextArea descriptionArea;
	private ComboBox courseCombo;
	private DateField startDateField;
	private DateField endDateField;
	private QuestionTable tableOfExistQuestion;
	private BeanItemContainer<Question> containerOfExistQuestion;
	private QuestionTable tableOfAddedQuestion;
	private BeanItemContainer<Question> containerOfAddedQuestion;

	private HorizontalLayout buttonLayout;
	private Button saveButton;
	private Button deleteButton;
	private Button cancelButton;
	private Label errorLabel;

	private ExamService examService;
	private Exam exam;

	public ExamForm(Exam exam) {
		this.exam = (exam != null) ? exam : new Exam();
		this.examService = AppContext.getExamService();
	}

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setHeight("100%");

		formLayout = buildFormLayout();
		mainLayout.addComponent(formLayout);

		return mainLayout;
	}

	private GridLayout buildFormLayout() {
		formLayout = new GridLayout(3, 5);
		formLayout.setSpacing(true);
		formLayout.setMargin(true);

//		HorizontalLayout firstRowLayout = new HorizontalLayout();
//		firstRowLayout.setSpacing(true);
//		firstRowLayout.setSizeFull();
//		formLayout.addComponent(firstRowLayout);

		nameField = buildNameField();
		formLayout.addComponent(nameField);

		descriptionArea = buildDescriptionArea();
		formLayout.addComponent(descriptionArea);

		courseCombo = buildCourseCombo();
		formLayout.addComponent(courseCombo);

		startDateField = buildDateField(Messages.getValue(GoesConstants.EXAM_START));
		formLayout.addComponent(startDateField);

		endDateField = buildDateField(Messages.getValue(GoesConstants.EXAM_END));
		formLayout.addComponent(endDateField);

		tableOfExistQuestion = buildTableOfExistQuestion();
		formLayout.addComponent(tableOfExistQuestion, 0, 2);

		tableOfAddedQuestion = buildTableOfAddedQuestion();
		formLayout.addComponent(tableOfAddedQuestion, 1, 2);

		errorLabel = buildErrorLabel();
		formLayout.addComponent(errorLabel, 1, 3);

		buttonLayout = buildButtonLayout();
		formLayout.addComponent(buttonLayout, 1, 4);

		return formLayout;
	}

	private TextField buildNameField() {
		nameField = new TextField();
		nameField.setCaption(Messages.getValue(GoesConstants.EXAM_NAME));
		nameField.setImmediate(true);
		nameField.setRequired(true);
		nameField.setNullRepresentation("");

		return nameField;
	}

	private TextArea buildDescriptionArea() {
		descriptionArea = new TextArea();
		descriptionArea.setCaption(Messages.getValue(GoesConstants.EXAM_DESCRIPTION));
		descriptionArea.setImmediate(true);
		descriptionArea.setRequired(true);
		descriptionArea.setNullRepresentation("");
		descriptionArea.setWidth("250px");
		descriptionArea.setHeight("60px");

		return descriptionArea;
	}
	
	private ComboBox buildCourseCombo() {
		courseCombo = new ComboBox(Messages.getValue(GoesConstants.USER_COURSE_NAME));
		Set<Course> courseList = ((Teacher) session.getUser()).getCourses();
		for (Course course : courseList) {
			courseCombo.addItem(course);
//			courseCombo.setItemCaption(course, course.getName());
		}
		return courseCombo;
	}

	private DateField buildDateField(String caption) {
		DateField dateField = new DateField(caption);
		dateField.setLocale(session.getLocale());
		dateField.setLenient(true);
		dateField.setImmediate(true);
		dateField.setRequired(true);
		dateField.setResolution(DateField.RESOLUTION_MIN);

		return dateField;
	}

	private QuestionTable buildTableOfExistQuestion() {
		tableOfExistQuestion = new QuestionTable();
		tableOfExistQuestion.setCaption(Messages.getValue(GoesConstants.EXAM_QUESTIONS_EXIST));
		tableOfExistQuestion.setWidth("400px");
		tableOfExistQuestion.setPageLength(TABLE_COUNT);
		tableOfExistQuestion.setDragMode(TableDragMode.ROW);
		tableOfExistQuestion.setDropHandler(new DropHandler() {
			@Override
			public AcceptCriterion getAcceptCriterion() {
				return new And(new SourceIs(tableOfAddedQuestion), AcceptItem.ALL);
			}

			@Override
			public void drop(DragAndDropEvent event) {
				dragAndDropExam(event);
			}
		});

		containerOfExistQuestion = tableOfExistQuestion.getContainerDataSource();
		containerOfExistQuestion.removeContainerProperty(QuestionTable.FIELDS);

		return tableOfExistQuestion;
	}

	private QuestionTable buildTableOfAddedQuestion() {
		tableOfAddedQuestion = new QuestionTable();
		tableOfAddedQuestion.setCaption(Messages.getValue(GoesConstants.EXAM_QUESTIONS_ADDED));
		tableOfAddedQuestion.setWidth("400px");
		tableOfAddedQuestion.setPageLength(TABLE_COUNT);
		tableOfAddedQuestion.setDragMode(TableDragMode.ROW);
		tableOfAddedQuestion.setDropHandler(new DropHandler() {
			@Override
			public AcceptCriterion getAcceptCriterion() {
				return AcceptItem.ALL;
			}

			@Override
			public void drop(DragAndDropEvent event) {
				dragAndDropExam(event);
			}
		});

		containerOfAddedQuestion = tableOfAddedQuestion.getContainerDataSource();
		containerOfAddedQuestion.removeContainerProperty(QuestionTable.FIELDS);

		return tableOfAddedQuestion;
	}

	private Label buildErrorLabel() {
		errorLabel = new Label("", Label.CONTENT_XHTML);
		errorLabel.setImmediate(true);

		return errorLabel;
	}

	private HorizontalLayout buildButtonLayout() {
		buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);

		saveButton = new Button(Messages.getValue(GoesConstants.BUTTON_SAVE), this);
		buttonLayout.addComponent(saveButton);

		deleteButton = new Button(Messages.getValue(GoesConstants.BUTTON_DELETE), this);
		buttonLayout.addComponent(deleteButton);

		cancelButton = new Button(Messages.getValue(GoesConstants.BUTTON_CANCEL), this);
		buttonLayout.addComponent(cancelButton);

		return buttonLayout;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();
		if (source == saveButton) {
			if (isValid()) {
				exam.setCreator((Teacher) session.getUser());
				examService.update(exam);
				mainWindow.showExamView();
				mainWindow.showNotification(Messages.getValue(GoesConstants.MESSAGE_SUCCESFULLY_SAVED));
			}
		} else if (source == deleteButton) {
			examService.delete(exam);
			mainWindow.showExamView();
			mainWindow.showNotification(Messages.getValue(GoesConstants.MESSAGE_SUCCESFULLY_DELETED));
		} else if (source == cancelButton) {
			ConfirmationDialogPopupWindow popupWindow = new ConfirmationDialogPopupWindow(Messages.getValue(GoesConstants.MESSAGE_DISCARD_CHANGES));
			popupWindow.addListener(new ConfirmationEventListener() {
				@Override
				protected void confirmed(ConfirmationEvent event) {
					mainWindow.showExamView();
				}

				@Override
				protected void rejected(ConfirmationEvent event) {
				}
			});
			mainWindow.addWindow(popupWindow);
		}
	}

	@Override
	public void refreshContent() {
		nameField.setValue(exam.getName());
		descriptionArea.setValue(exam.getDescription());
		courseCombo.setValue(exam.getCourse());
		startDateField.setValue(exam.getStart());
		endDateField.setValue(exam.getEnd());

		List<Question> existQuestions = AppContext.getQuestionService().findAll();
		for (Question addedQuestion : exam.getQuestions()) {
			for (Iterator<Question> itr = existQuestions.iterator(); itr.hasNext();) {
				Question existQuestion = itr.next();
				if (addedQuestion.getId() == existQuestion.getId()) {
					itr.remove();
					break;
				}
			}
		}
		containerOfExistQuestion.removeAllItems();
		containerOfExistQuestion.addAll(existQuestions);
		containerOfAddedQuestion.removeAllItems();
		containerOfAddedQuestion.addAll(exam.getQuestions());
	}

	private void dragAndDropExam(DragAndDropEvent dropEvent) {
		AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent.getTargetDetails());
		DataBoundTransferable t = (DataBoundTransferable) dropEvent.getTransferable();

		Component c = t.getSourceComponent();
		DropTarget dt = dropEvent.getTargetDetails().getTarget();
		if ((c != null && c instanceof QuestionTable) && (dt != null && dt instanceof QuestionTable)) {
			QuestionTable sourceTable = (QuestionTable) c;
			BeanItemContainer<Question> sourceContainer = sourceTable.getContainerDataSource();
			QuestionTable targetTable = (QuestionTable) dt;
			BeanItemContainer<Question> targetContainer = targetTable.getContainerDataSource();

			Object sourceItem = t.getItemId();
			Object targetItem = dropData.getItemIdOver();
			if (sourceItem != null && sourceItem instanceof Question && !targetContainer.containsId(sourceItem)) {
				if (targetItem != null && targetItem instanceof Question) {
					if (dropData.getDropLocation() == VerticalDropLocation.BOTTOM) {
						sourceContainer.removeItem(sourceItem);
						targetContainer.addItemAfter(targetItem, sourceItem);
					} else {
						sourceContainer.removeItem(sourceItem);
						Object prevItemId = targetContainer.prevItemId(targetItem);
						targetContainer.addItemAfter(prevItemId, sourceItem);
					}
				} else if (targetItem == null) {
					sourceContainer.removeItem(sourceItem);
					targetContainer.addItem(sourceItem);
				}
			}
		}
	}

	private boolean isValid() {
		String name = (String) nameField.getValue();
		if (name != null && !name.equals(""))
			exam.setName(name);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.EXAM_NAME)));
			return false;
		}

		String description = (String) descriptionArea.getValue();
		if (description != null && !description.equals(""))
			exam.setDescription(description);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.EXAM_DESCRIPTION)));
			return false;
		}

		Course course = (Course) courseCombo.getValue();
		if (course != null)
			exam.setCourse(course);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.EXAM_COURSE)));
			return false;
		}

		Date start = (Date) startDateField.getValue();
		if (start != null)
			exam.setStart(start);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.EXAM_START)));
			return false;
		}

		Date end = (Date) endDateField.getValue();
		if (end != null)
			exam.setEnd(end);
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.EXAM_END)));
			return false;
		}

		Collection<Question> questions = tableOfAddedQuestion.getContainerDataSource().getItemIds();
		if (questions != null && !questions.isEmpty())
			exam.setQuestions(new HashSet<Question>(questions));
		else {
			errorLabel.setValue(buildErrorMessage(Messages.getValue(GoesConstants.EXAM_QUESTIONS_ADDED)));
			return false;
		}
		return true;
	}

	private String buildErrorMessage(String fieldName) {
		return "<span style='color:red'>" + Messages.getValue(GoesConstants.MESSAGE_IS_NOT_VALID, fieldName) + "</span>";
	}
}
