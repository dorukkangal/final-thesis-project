package tr.edu.gsu.view.page;

import tr.edu.gsu.session.GoesSession;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.view.MainWindow;
import tr.edu.gsu.view.toolbar.ToolBar;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public abstract class BasePage extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	protected GoesSession session;
	protected MainWindow mainWindow;

	protected VerticalLayout mainLayout;
	protected ToolBar toolBar;

	public BasePage() {
		super();
		this.session = AppContext.getSession();
		this.mainWindow = AppContext.getMainWindow();
	}

	@Override
	public void attach() {
		super.attach();
		init();
		refreshContent();
	}

	protected void init() {
		removeAllComponents();
		toolBar = buildToolBar();
		addComponent(toolBar);

		mainLayout = buildMainLayout();
		Panel container = new Panel(mainLayout);
		container.setSizeFull();
		addComponent(container);
		setExpandRatio(container, 1.0f);
		setSizeFull();
	}

	public ToolBar buildToolBar() {
		return toolBar = new ToolBar();
	}

	public abstract VerticalLayout buildMainLayout();

	public abstract void refreshContent();
}
