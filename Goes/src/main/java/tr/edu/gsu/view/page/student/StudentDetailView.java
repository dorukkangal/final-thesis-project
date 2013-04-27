package tr.edu.gsu.view.page.student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.user.Student;
import tr.edu.gsu.domain.user.StudentExam;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.service.user.StudentService;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.ChartUtil;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.util.ImageUtil;
import tr.edu.gsu.view.decoration.BlueBar;
import tr.edu.gsu.view.page.BasePage;

import com.invient.vaadin.charts.InvientCharts;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class StudentDetailView extends BasePage {
	private Panel profileContainer;
	private GridLayout profileLayout;
	private Embedded pictureContainer;
	private BlueBar fullName;
	private TextField emailField;
	private TextField departmentField;
	private TextField phoneField;
	private TextField academicRankField;

	private Table participatedExamTable;
	private ColumnGenerator columnGenerator;
	private BeanItemContainer<Exam> container;

	private static final String EXAM_NAME = "name";
	private static final Object[] VISIBLE_COLUMNS = new Object[] { EXAM_NAME };

	private InvientCharts examResultChart;

	private Student student;
	private StudentService studentService;

	public StudentDetailView(Student student) {
		super();
		this.student = student;
		this.studentService = AppContext.getStudentService();
		init();
	}

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		if (student != null) {
			profileContainer = buildProfileContainer();
			mainLayout.addComponent(profileContainer);
			mainLayout.setComponentAlignment(profileContainer, Alignment.MIDDLE_CENTER);
		}
		return mainLayout;
	}

	private Panel buildProfileContainer() {
		profileContainer = new Panel();
		profileContainer.setSizeFull();
		profileContainer.setScrollable(true);

		VerticalLayout content = (VerticalLayout) profileContainer.getContent();
		content.setWidth("100%");

		profileLayout = buildProfileLayout();
		content.addComponent(profileLayout);
		content.setComponentAlignment(profileLayout, Alignment.MIDDLE_CENTER);

		return profileContainer;
	}

	private GridLayout buildProfileLayout() {
		profileLayout = new GridLayout(4, 6);
		profileLayout.setSpacing(true);
		profileLayout.setMargin(true);

		Resource picture = ImageUtil.resizeImage(student.getPicture(), 150, true);
		if (picture == null)
			picture = new ThemeResource("images/profile.img");
		pictureContainer = new Embedded("", picture);
		profileLayout.addComponent(pictureContainer, 0, 0, 0, 1);

		fullName = new BlueBar(student.getFullName());
		profileLayout.addComponent(fullName, 1, 0, 2, 0);
		profileLayout.setComponentAlignment(fullName, Alignment.BOTTOM_CENTER);

		emailField = buildField(Messages.getValue(GoesConstants.USER_EMAIL), student.getEmail());
		profileLayout.addComponent(emailField, 1, 1);

		departmentField = buildField(Messages.getValue(GoesConstants.USER_FACULTY), student.getDepartment().getName());
		profileLayout.addComponent(departmentField, 2, 1);

		phoneField = buildField(Messages.getValue(GoesConstants.USER_PHONE), student.getPhoneNumber());
		profileLayout.addComponent(phoneField, 1, 2);

		academicRankField = buildField(Messages.getValue(GoesConstants.USER_CLASS), student.getAcademicRank());
		profileLayout.addComponent(academicRankField, 2, 2);

		profileLayout.addComponent(new BlueBar(Messages.getValue(GoesConstants.DASHBOARD)), 0, 3, 3, 3);

		participatedExamTable = buildStudentExamTable();
		participatedExamTable.setWidth("350px");
		participatedExamTable.setHeight("350px");
		profileLayout.addComponent(participatedExamTable, 0, 4, 1, 5);

		refreshExamResultChart(null);

		return profileLayout;
	}

	private TextField buildField(String caption, String value) {
		TextField field = new TextField(caption);
		field.setNullRepresentation("");
		field.setValue(value);
		field.setStyleName("changeBorder");
		field.setReadOnly(true);

		return field;
	}

	private Table buildStudentExamTable() {
		participatedExamTable = new Table();
		participatedExamTable.setSizeFull();
		participatedExamTable.setImmediate(true);
		participatedExamTable.setSelectable(true);

		container = new BeanItemContainer<Exam>(Exam.class);
		participatedExamTable.setContainerDataSource(container);

		columnGenerator = new StudentTableColumnGenerator();
		List<Object> propertyIds = new ArrayList<Object>(participatedExamTable.getContainerPropertyIds());
		List<Object> columns = Arrays.asList(VISIBLE_COLUMNS);
		for (Object propertyId : propertyIds) {
			if (columns.contains(propertyId))
				participatedExamTable.addGeneratedColumn(propertyId, columnGenerator);
			else
				container.removeContainerProperty(propertyId);
		}
		participatedExamTable.setVisibleColumns(VISIBLE_COLUMNS);
		participatedExamTable.setColumnHeaders(new String[] { Messages.getValue(GoesConstants.EXAM_NAME) });
		participatedExamTable.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				Exam selected = (Exam) event.getProperty().getValue();
				refreshExamResultChart(selected);
			}
		});
		return participatedExamTable;
	}

	@Override
	public void refreshContent() {
		participatedExamTable.removeAllItems();
		Set<StudentExam> studentExamList = student.getParticipatedExams();
		for (StudentExam studentExam : studentExamList)
			if (!container.containsId(studentExam.getExam()))
				container.addItem(studentExam.getExam());
		participatedExamTable.select(container.getIdByIndex(0));
	}

	private void refreshExamResultChart(Exam exam) {
		List<StudentExam> examResult = studentService.findParticipatedExamResults(student, exam);
		if (examResultChart != null)
			profileLayout.removeComponent(examResultChart);
		examResultChart = ChartUtil.buildExamResultChart(examResult);
		examResultChart.setWidth("350px");
		examResultChart.setHeight("350px");
		profileLayout.addComponent(examResultChart, 2, 4, 3, 5);
	}

	private class StudentTableColumnGenerator implements ColumnGenerator {

		@Override
		public Object generateCell(Table source, Object itemId, Object columnId) {
			Exam exam = (Exam) itemId;
			switch ((String) columnId) {
			case EXAM_NAME:
				return exam.getName();
			}
			return null;
		}
	}
}
