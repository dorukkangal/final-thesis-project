package tr.edu.gsu.view.page.question;

import java.util.Collection;

import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.domain.exam.QuestionField;
import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.ui.Table;

@SuppressWarnings("serial")
public class QuestionTable extends Table implements Action.Handler {

	private static final Action EDIT = new Action(Messages.getValue(GoesConstants.ACTION_DETAIL));
	private static final Action[] ACTIONS = new Action[] { EDIT };

	public static final String NAME = "name";
	public static final String TIME = "time";
	public static final String POINT = "point";
	public static final String CREATOR = "creator";
	public static final String FIELDS = "fields";

	private ColumnGenerator columnGenerator;
	private BeanItemContainer<Question> container;

	public QuestionTable() {
		this.setSizeFull();
		this.setImmediate(true);
		this.setSelectable(true);
		container = new BeanItemContainer<Question>(Question.class);
		this.setContainerDataSource(container);
		this.addActionHandler(this);
		this.setVisibleColumns(new String[] { NAME, TIME, POINT, CREATOR, FIELDS });
		this.setColumnHeaders(new String[] { Messages.getValue(GoesConstants.QUESTION_NAME), Messages.getValue(GoesConstants.QUESTION_TIME),
				Messages.getValue(GoesConstants.QUESTION_POINT), Messages.getValue(GoesConstants.QUESTION_CREATOR), Messages.getValue(GoesConstants.QUESTION_FIELDS) });
		this.removeContainerProperty("id");
		this.setDragMode(TableDragMode.ROW);

		columnGenerator = new QuestionTableColumnGenerator();
		Collection<?> propertyIds = this.getContainerPropertyIds();
		for (Object propertyId : propertyIds)
			this.addGeneratedColumn(propertyId, columnGenerator);
	}

	@Override
	public BeanItemContainer<Question> getContainerDataSource() {
		return container;
	}

	@Override
	public Action[] getActions(Object target, Object sender) {
		return ACTIONS;
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		Question question = (Question) ((BeanItem<?>) QuestionTable.this.getItem(target)).getBean();
		AppContext.getMainWindow().showQuestionForm(false, question);
	}

	private class QuestionTableColumnGenerator implements ColumnGenerator {

		@Override
		public Object generateCell(Table source, Object itemId, Object columnId) {
			Question question = (Question) itemId;
			switch ((String) columnId) {
			case NAME:
				return (question.getName() != null) ? question.getName() : "";
			case TIME:
				return (question.getTime() != null) ? question.getTime().toString() : "";
			case CREATOR:
				return (question.getCreator() != null) ? question.getCreator().getFullName() : "";
			case POINT:
				return question.getPoint();
			case FIELDS:
				int choiceCount = 0;
				int imageCount = 0;
				int soundCount = 0;
				int videoCount = 0;
				for (QuestionField field : question.getFields()) {
					if (field.getType().equals(GoesConstants.QUESTION_CHOICE))
						choiceCount++;
					else if (field.getType().equals(GoesConstants.QUESTION_IMAGE))
						imageCount++;
					else if (field.getType().equals(GoesConstants.QUESTION_SOUND))
						soundCount++;
					else if (field.getType().equals(GoesConstants.QUESTION_VIDEO))
						videoCount++;
				}
				return Messages.getValue(GoesConstants.QUESTION_FIELD_MESSAGE, choiceCount, imageCount, soundCount, videoCount);
			}
			return null;
		}
	}
}
