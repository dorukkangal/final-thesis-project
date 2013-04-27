package tr.edu.gsu.view.decoration;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class BlueBar extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private Label captionLabel;
	private HorizontalLayout blueLayout;

	public BlueBar(String caption) {
		this();
		captionLabel.setValue(caption);
	}

	public BlueBar() {
		captionLabel = new Label("", Label.CONTENT_XHTML);
		captionLabel.setStyleName(Reindeer.LABEL_H2);
		addComponent(captionLabel);

		blueLayout = new HorizontalLayout();
		blueLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
		blueLayout.setHeight(3, Sizeable.UNITS_PIXELS);
		blueLayout.setStyleName("v-horizontal-bar");
		addComponent(blueLayout);
	}
}
