package tr.edu.gsu.view.page.message;

import tr.edu.gsu.view.page.BasePage;
import tr.edu.gsu.view.toolbar.ToolBar;

import com.vaadin.ui.VerticalLayout;

public class MessageView extends BasePage {
	private static final long serialVersionUID = 1L;

	@Override
	public ToolBar buildToolBar() {
		return toolBar = new ToolBar();
	}

	@Override
	public VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();

		return mainLayout;
	}

	@Override
	public void refreshContent() {
		// TODO Auto-generated method stub

	}
}