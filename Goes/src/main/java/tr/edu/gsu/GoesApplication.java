package tr.edu.gsu;

import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;
import tr.edu.gsu.view.MainWindow;

import com.vaadin.Application;

public class GoesApplication extends Application {
	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow;

	@Override
	public void init() {
		setMainWindow(mainWindow);
		setTheme(GoesConstants.THEME_GOES);

		// silinecek
		mainWindow = AppContext.getMainWindow();
//		DemoCreator.createDemo();
		AppContext.getSession().setUser(AppContext.getTeacherService().find("burak.arslan@gsu.edu.tr", "111"));
		mainWindow.attach();
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
}