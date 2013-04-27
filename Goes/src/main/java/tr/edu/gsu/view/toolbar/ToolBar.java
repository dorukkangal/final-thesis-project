package tr.edu.gsu.view.toolbar;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.edu.gsu.util.GoesConstants;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class ToolBar extends HorizontalLayout {
	private static Logger logger = LoggerFactory.getLogger(ToolBar.class);

	protected Map<String, Entry> entryMap;
	protected Entry currentEntry;

	public ToolBar() {
		entryMap = new HashMap<String, Entry>();

		setWidth("100%");
		setHeight(36, UNITS_PIXELS);
		addStyleName(GoesConstants.STYLE_TOOLBAR);

		// Add label to fill excess space
		Label spacer = new Label();
		spacer.setContentMode(Label.CONTENT_XHTML);
		spacer.setValue("&nbsp;");
		addComponent(spacer);
		setExpandRatio(spacer, 1.0f);
	}

	public Entry addToolbarEntry(String key, String title, ToolbarCommand command) {
		if (entryMap.containsKey(key)) {
			IllegalArgumentException exception = new IllegalArgumentException("Toolbar already contains entry for key: " + key);
			logger.error(exception.getMessage(), exception);
			throw exception;
		}

		Entry entry = new Entry(key, title);
		if (command != null) {
			entry.setCommand(command);
		}

		entryMap.put(key, entry);
		addEntryComponent(entry);
		return entry;
	}

	protected void disableToolbarEntry(String key) {
		if (!entryMap.containsKey(key)) {
			IllegalArgumentException exception = new IllegalArgumentException("Toolbar does not contain entry for key: " + key);
			logger.error(exception.getMessage(), exception);
			throw exception;
		}

		entryMap.get(key).setEnabled(false);
	}

	public PopupEntry addPopupEntry(String key, String title) {
		if (entryMap.containsKey(key)) {
			IllegalArgumentException exception = new IllegalArgumentException("Toolbar already contains entry for key: " + key);
			logger.error(exception.getMessage(), exception);
			throw exception;
		}

		PopupEntry entry = new PopupEntry(key, title);
		entryMap.put(key, entry);
		addEntryComponent(entry);
		return entry;
	}

	public long getCount(String key) {
		Entry toolbarEntry = entryMap.get(key);
		if (toolbarEntry == null) {
			IllegalArgumentException exception = new IllegalArgumentException("Toolbar doesn't contain an entry for key: " + key);
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
		return toolbarEntry.getCount();
	}

	public void setCount(String key, Long count) {
		Entry toolbarEntry = entryMap.get(key);
		if (toolbarEntry == null) {
			IllegalArgumentException exception = new IllegalArgumentException("Toolbar doesn't contain an entry for key: " + key);
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
		toolbarEntry.setCount(count);
	}

	public Entry getEntry(String key) {
		return entryMap.get(key);
	}

	public synchronized void setActiveEntry(String key) {
		if (currentEntry != null) {
			currentEntry.setActive(false);
		}

		currentEntry = entryMap.get(key);
		if (currentEntry != null) {
			currentEntry.setActive(true);
		}
	}

	protected void addEntryComponent(Entry entry) {
		addComponent(entry, getComponentCount() - 1);
		setComponentAlignment(entry, Alignment.MIDDLE_LEFT);
	}

	public class Entry extends CustomComponent {
		protected String key;
		protected String title;
		protected Long count;
		protected ToolbarCommand command;
		protected boolean active;

		protected Button titleButton;
		protected Button countButton;
		protected HorizontalLayout layout;

		private Entry(String key, String title) {
			this.key = key;
			this.title = title;

			layout = new HorizontalLayout();
			layout.setMargin(false, true, false, false);
			setCompositionRoot(layout);
			setSizeUndefined();
			addStyleName(GoesConstants.STYLE_TOOLBAR_CLICKABLE);

			initLabelComponent();
			initCountComponent();
		}

		private void initLabelComponent() {
			titleButton = new Button(title);
			titleButton.addStyleName(Reindeer.BUTTON_LINK);
			layout.addComponent(titleButton);
			layout.setComponentAlignment(titleButton, Alignment.MIDDLE_LEFT);

			titleButton.addListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					if (command != null) {
						command.toolBarItemSelected(Entry.this);
					}
				}
			});
		}

		private void initCountComponent() {
			countButton = new Button(count + "");
			countButton.addStyleName(Reindeer.BUTTON_LINK);
			countButton.addStyleName(GoesConstants.STYLE_TOOLBAR_COUNT);

			// Initially hidden
			countButton.setVisible(false);

			layout.addComponent(countButton);
			layout.setComponentAlignment(countButton, Alignment.MIDDLE_LEFT);
		}

		public void setCount(Long count) {
			this.count = count;
			if (count != null) {
				countButton.setCaption(count + "");
				if (!countButton.isVisible()) {
					countButton.setVisible(true);
				}
			} else {
				countButton.setVisible(true);
			}
		}

		public Long getCount() {
			return count;
		}

		public void setActive(boolean active) {
			if (this.active != active) {
				this.active = active;
				if (active) {
					titleButton.addStyleName(GoesConstants.STYLE_TOOLBAR_ACTIVE);
					countButton.addStyleName(GoesConstants.STYLE_TOOLBAR_ACTIVE);
				} else {
					titleButton.removeStyleName(GoesConstants.STYLE_TOOLBAR_ACTIVE);
					countButton.removeStyleName(GoesConstants.STYLE_TOOLBAR_ACTIVE);
				}
			}
		}

		private void setCommand(ToolbarCommand command) {
			this.command = command;
		}
	}

	public class PopupEntry extends Entry {
		private static final long serialVersionUID = 1L;

		protected MenuBar menuBar;
		protected MenuItem rootItem;

		private PopupEntry(String key, String title) {
			super(key, title);
		}

		public MenuItem addMenuItem(String title) {
			return rootItem.addItem(title, null);
		}

		public MenuItem addMenuItem(String title, final ToolbarCommand command) {
			return rootItem.addItem(title, new Command() {
				private static final long serialVersionUID = 1L;

				public void menuSelected(MenuItem selectedItem) {
					if (command != null) {
						command.toolBarItemSelected(PopupEntry.this);
					}
				}
			});
		}

		public MenuItem addMenuItem(String title, Resource icon) {
			return rootItem.addItem(title, icon, null);
		}

		public MenuItem addMenuItem(String title, Resource icon, final ToolbarCommand command) {
			return rootItem.addItem(title, icon, new Command() {
				private static final long serialVersionUID = 1L;

				@Override
				public void menuSelected(MenuItem selectedItem) {
					if (command != null)
						command.toolBarItemSelected(PopupEntry.this);
				}
			});
		}

		@Override
		public void setActive(boolean active) {
			if (this.active != active) {
				this.active = active;
				if (active) {
					menuBar.addStyleName(GoesConstants.STYLE_TOOLBAR_ACTIVE);
					countButton.addStyleName(GoesConstants.STYLE_TOOLBAR_ACTIVE);
				} else {
					menuBar.removeStyleName(GoesConstants.STYLE_TOOLBAR_ACTIVE);
					countButton.removeStyleName(GoesConstants.STYLE_TOOLBAR_ACTIVE);
				}
			}
		}

		protected void initLabelComponent() {
			menuBar = new MenuBar();
			menuBar.addStyleName(GoesConstants.STYLE_TOOLBAR_POPUP);
			rootItem = menuBar.addItem(title, null);
			layout.addComponent(menuBar);
		}
	}

	public interface ToolbarCommand {
		void toolBarItemSelected(Entry toolbarEntry);
	}
}
