package tr.edu.gsu.view.page.student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tr.edu.gsu.domain.user.Student;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.page.BasePage;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class StudentView extends BasePage implements Action.Handler {
	private static final Action EDIT = new Action(Messages.getValue(GoesConstants.ACTION_DETAIL));
	private static final Action[] ACTIONS = new Action[] { EDIT };

	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String EMAIL = "email";
	private static final String DEPARTMENT = "department";
	private static final Object[] VISIBLE_COLUMNS = new Object[] { FIRST_NAME, LAST_NAME, EMAIL, DEPARTMENT };

	private Table studentTable;
	private List<Student> studentList;
	private BeanItemContainer<Student> container;
	private ColumnGenerator columnGenerator;

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		studentTable = buildStudentTable();
		mainLayout.addComponent(studentTable);

		return mainLayout;
	}

	private Table buildStudentTable() {
		studentTable = new Table();
		container = new BeanItemContainer<Student>(Student.class);
		studentTable.setContainerDataSource(container);
		studentTable.setSizeFull();
		studentTable.setImmediate(true);
		studentTable.setSelectable(true);
		studentTable.addActionHandler(this);
		columnGenerator = new StudentTableColumnGenerator();
		List<Object> propertyIds = new ArrayList<Object>(studentTable.getContainerPropertyIds());
		List<Object> columns = Arrays.asList(VISIBLE_COLUMNS);
		for (Object propertyId : propertyIds) {
			if (columns.contains(propertyId))
				studentTable.addGeneratedColumn(propertyId, columnGenerator);
			else
				container.removeContainerProperty(propertyId);
		}
		studentTable.setVisibleColumns(VISIBLE_COLUMNS);
		studentTable.setColumnHeaders(new String[] { Messages.getValue(GoesConstants.USER_FIRST_NAME), Messages.getValue(GoesConstants.USER_LAST_NAME),
				Messages.getValue(GoesConstants.USER_EMAIL), Messages.getValue(GoesConstants.USER_FACULTY) });
		return studentTable;
	}

	@Override
	public void refreshContent() {
		studentTable.removeAllItems();
		studentList = AppContext.getStudentService().findAll();
		container.addAll(studentList);
	}

	@Override
	public Action[] getActions(Object target, Object sender) {
		return ACTIONS;
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		Student student = container.getItem(target).getBean();
		mainWindow.showStudentDetailView(student);
	}

	private class StudentTableColumnGenerator implements ColumnGenerator {

		@Override
		public Object generateCell(Table source, Object itemId, Object columnId) {
			Student student = (Student) itemId;
			switch ((String) columnId) {
			case FIRST_NAME:
				return student.getFirstName();
			case LAST_NAME:
				return student.getLastName();
			case EMAIL:
				return student.getEmail();
			case DEPARTMENT:
				return student.getDepartment().getName();
			}
			return null;
		}
	}
}
