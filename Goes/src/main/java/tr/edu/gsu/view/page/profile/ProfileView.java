package tr.edu.gsu.view.page.profile;

import java.util.Set;

import tr.edu.gsu.domain.exam.Course;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.ChartUtil;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.util.ImageUtil;
import tr.edu.gsu.view.decoration.BlueBar;
import tr.edu.gsu.view.page.BasePage;

import com.invient.vaadin.charts.InvientCharts;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ProfileView extends BasePage {
	private Panel profileContainer;
	private GridLayout profileLayout;
	private Embedded pictureContainer;
	private BlueBar fullName;
	private TextField emailField;
	private TextField departmentField;
	private TextField phoneField;
	private TextField academicRankField;
	private Table courseTable;
	private InvientCharts examChart;
	private InvientCharts questionChart;

	private Teacher teacher;

	public ProfileView(Teacher teacher) {
		super();
		this.teacher = teacher;
		init();
	}

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		if (teacher != null) {
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

		Resource picture = ImageUtil.resizeImage(teacher.getPicture(), 150, true);
		if (picture == null)
			picture = new ThemeResource("images/profile.img");
		pictureContainer = new Embedded("", picture);
		profileLayout.addComponent(pictureContainer, 0, 0, 0, 1);

		fullName = new BlueBar(teacher.getFullName());
		profileLayout.addComponent(fullName, 1, 0, 3, 0);
		profileLayout.setComponentAlignment(fullName, Alignment.BOTTOM_CENTER);

		emailField = buildField(Messages.getValue(GoesConstants.USER_EMAIL), teacher.getEmail());
		profileLayout.addComponent(emailField, 1, 1);

		departmentField = buildField(Messages.getValue(GoesConstants.USER_FACULTY), teacher.getDepartment().getName());
		profileLayout.addComponent(departmentField, 2, 1);

		phoneField = buildField(Messages.getValue(GoesConstants.USER_PHONE), teacher.getPhoneNumber());
		profileLayout.addComponent(phoneField, 1, 2);

		academicRankField = buildField(Messages.getValue(GoesConstants.USER_ACADEMIC_RANK), teacher.getAcademicRank());
		profileLayout.addComponent(academicRankField, 2, 2);

		courseTable = buildCourseTable(teacher.getCourses());
		profileLayout.addComponent(courseTable, 3, 1, 3, 2);

		profileLayout.addComponent(new BlueBar(Messages.getValue(GoesConstants.DASHBOARD)), 0, 3, 3, 3);

		examChart = ChartUtil.buildExamChart();
		examChart.setWidth("350px");
		examChart.setHeight("350px");
		profileLayout.addComponent(examChart, 0, 4, 1, 5);

		questionChart = ChartUtil.buildQuestionChart();
		questionChart.setWidth("350px");
		questionChart.setHeight("350px");
		profileLayout.addComponent(questionChart, 2, 4, 3, 5);

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

	private Table buildCourseTable(Set<Course> courses) {
		courseTable = new Table(Messages.getValue(GoesConstants.USER_COURSES));
		courseTable.addContainerProperty("name", String.class, "");
		courseTable.setColumnHeader("name", Messages.getValue(GoesConstants.USER_COURSE_NAME));
		courseTable.setPageLength( courses.size() );
		for (Course course : courses)
			courseTable.addItem(new Object[] { course.getName() }, course.getName());
		return courseTable;
	}

	@Override
	public void refreshContent() {
		// fullName.setValue(teacher.getFullName());
		// emailField.setValue(teacher.getEmail());
		// departmentField.setValue(teacher.getDepartment().getName());
	}
}
