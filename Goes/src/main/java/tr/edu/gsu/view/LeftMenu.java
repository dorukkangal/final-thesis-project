package tr.edu.gsu.view;

import java.util.Iterator;

import tr.edu.gsu.i18n.Messages;
import tr.edu.gsu.util.GoesConstants;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

@SuppressWarnings("serial")
public class LeftMenu extends CssLayout implements LayoutClickListener {
	private Tab selectedTab = null;

	@Override
	public void attach() {
		super.attach();
		setStyleName(GoesConstants.STYLE_MELODION);
		addStyleName(GoesConstants.STYLE_SIDEBAR_MENU);
		setSizeFull();
	}

	public Tab addTab(String caption, Resource icon, boolean addNewButton) {
		Tab tab = new Tab(caption, icon, addNewButton);
		tab.addListener(this);
		addComponent(tab);
		return tab;
	}

	public Component addSpacer() {
		Component spacer = new Label();
		spacer.setStyleName(GoesConstants.STYLE_LEFTMENU_SPACER);
		addComponent(spacer);
		return spacer;
	}

	public Tab getSelected() {
		return selectedTab;
	}

	public void setSelected(Component c) {
		if (c != null) {
			updateStyles();
			Tab t = null;
			if (c instanceof Tab)
				t = (Tab) c;
			else
				t = (Tab) c.getParent();
			t.addStyleName(GoesConstants.STYLE_LEFTMENU_SELECTED_TAB);
			t.getLabel().addStyleName(GoesConstants.STYLE_LEFTMENU_SELECTED_TAB);
			t.getNewButton().addStyleName(GoesConstants.STYLE_LEFTMENU_SELECTED_TAB);
			t.getNewButton().setVisible(true);
			selectedTab = t;
		}
	}

	private void updateStyles() {
		for (Iterator<Component> itr = getComponentIterator(); itr.hasNext();) {
			Component c = itr.next();
			if (c instanceof Tab) {
				Tab t = ((Tab) c);
				t.removeStyleName(GoesConstants.STYLE_LEFTMENU_SELECTED_TAB);
				t.getLabel().removeStyleName(GoesConstants.STYLE_LEFTMENU_SELECTED_TAB);
				t.getNewButton().removeStyleName(GoesConstants.STYLE_LEFTMENU_SELECTED_TAB);
				t.getNewButton().setVisible(false);
			}
		}
	}

	@Override
	public void layoutClick(LayoutClickEvent event) {
		setSelected(event.getChildComponent());
	}

	public class Tab extends HorizontalLayout {
		private final Label label;
		private final NativeButton newButton;

		private Tab(String caption, Resource icon, boolean addNewButton) {
			label = new Label(caption);
			label.setIcon(icon);
			addComponent(label);
			setExpandRatio(label, 1.0f);
			setWidth("100%");

			newButton = new NativeButton(Messages.getValue(GoesConstants.BUTTON_NEW));
			newButton.addStyleName(GoesConstants.STYLE_LEFTMENU_BUTTON_NEW);
			newButton.setVisible(false);
			if (addNewButton) {
				addComponent(newButton);
				setComponentAlignment(newButton, Alignment.MIDDLE_LEFT);
			}
		}

		public Label getLabel() {
			return label;
		}
		
		public NativeButton getNewButton() {
			return newButton;
		}
	}
}
