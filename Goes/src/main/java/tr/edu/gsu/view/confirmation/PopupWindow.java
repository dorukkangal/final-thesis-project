package tr.edu.gsu.view.confirmation;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class PopupWindow extends Window {
	private static final long serialVersionUID = 1L;

	public PopupWindow() {
		super();
		init();
	}

	public PopupWindow(String caption) {
		super(caption);
		init();
	}

	private void init() {
		setStyleName(Reindeer.WINDOW_LIGHT);
		addStyleName("warning");
		setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
		setModal(true);
	}
}
