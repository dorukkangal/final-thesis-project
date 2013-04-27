package tr.edu.gsu.session;

import java.util.Locale;

import tr.edu.gsu.domain.user.GoesUser;
import tr.edu.gsu.util.GoesConstants;

public class GoesSessionImpl implements GoesSession {
	private static final long serialVersionUID = 1L;

	private GoesUser currentUser;
	private Locale locale = GoesConstants.LOCALE_TURKISH;

	public GoesUser getUser() {
		return currentUser;
	}

	public void setUser(GoesUser currentUser) {
		this.currentUser = currentUser;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
