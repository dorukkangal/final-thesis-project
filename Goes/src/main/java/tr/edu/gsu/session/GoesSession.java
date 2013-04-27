package tr.edu.gsu.session;

import java.io.Serializable;
import java.util.Locale;

import tr.edu.gsu.domain.user.GoesUser;

public interface GoesSession extends Serializable {

	public GoesUser getUser();

	public void setUser(GoesUser user);

	public Locale getLocale();

	public void setLocale(Locale locale);
}
