package tr.edu.gsu.view.page.question;

import java.util.List;

import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.view.page.BasePage;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.VerticalLayout;

public class QuestionView extends BasePage {
	private static final long serialVersionUID = 1L;

	private QuestionTable questionTable;
	private List<Question> questionList;

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		questionTable = new QuestionTable();
		mainLayout.addComponent(questionTable);

		return mainLayout;
	}

	@Override
	public void refreshContent() {
		questionTable.removeAllItems();
		questionList = AppContext.getQuestionService().findAll();
		BeanItemContainer<Question> container = questionTable.getContainerDataSource();
		container.removeAllItems();
		container.addAll(questionList);
	}
}
