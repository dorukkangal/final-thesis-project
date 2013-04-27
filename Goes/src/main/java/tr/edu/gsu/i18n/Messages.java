package tr.edu.gsu.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;

public class Messages {
	private static Log LOG = LogFactory.getLog(Messages.class);
	private static String BUNDLE_NAME = "messages";

	private static ResourceBundle bundle;

	static {
		initDefaultBundle();
	}

	public static void initBundle(Locale locale) {
		try {
			AppContext.getSession().setLocale(locale);
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			initDefaultBundle();
		}
	}

	public static void initDefaultBundle() {
		bundle = ResourceBundle.getBundle(BUNDLE_NAME, GoesConstants.LOCALE_TURKISH);
	}

	public static String getValue(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return "!" + key + "!";
		}
	}

	public static String getValue(String key, Object... arguments) {
		return MessageFormat.format(Messages.getValue(key), arguments);
	}
}
