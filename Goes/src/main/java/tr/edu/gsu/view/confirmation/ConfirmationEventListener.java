package tr.edu.gsu.view.confirmation;

import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;

public abstract class ConfirmationEventListener implements Listener {
	private static final long serialVersionUID = 1L;

	public final void componentEvent(Event event) {
		if (event instanceof ConfirmationEvent) {
			ConfirmationEvent ce = (ConfirmationEvent) event;
			if (ce.isConfirmed()) {
				confirmed(ce);
			} else {
				rejected(ce);
			}
		}
	}

	/**
	 * Called when confirmation is given.
	 */
	protected void confirmed(ConfirmationEvent event) {
	}

	/**
	 * Called when rejection is given.
	 */
	protected void rejected(ConfirmationEvent event) {
	}
}
