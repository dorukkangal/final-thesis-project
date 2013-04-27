package tr.edu.gsu.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface GoesConstants {
	// icons
	public static final String ICON_SEPERATOR = "img/icon_separator.gif";
	public static final String ICON_LOGO = "img/aaa.png";

	public static final String ICON_EXAM_MENU = "img/exam.jpg";
	public static final String ICON_QUESTION_MENU = "img/question.png";
	public static final String ICON_STUDENT_MENU = "img/student.png";
	public static final String ICON_DELETE = "img/delete.png";

	// styles
	public static final String THEME_GOES = "goes";
	public static final String STYLE_SELECTED_LANGUAGE = "selectLanguage";
	public static final String STYLE_MELODION = "melodion";
	public static final String STYLE_SIDEBAR_MENU = "sidebar-menu";
	public static final String STYLE_LEFTMENU_SPACER = "myspacer";
	public static final String STYLE_LEFTMENU_SELECTED_TAB = "selected";
	public static final String STYLE_LEFTMENU_BUTTON_NEW = "new";
	public static final String STYLE_TOOLBAR = "toolbar";
	public static final String STYLE_TOOLBAR_BUTTON = "toolbar-button";
	public static final String STYLE_TOOLBAR_CLICKABLE = "clickable";
	public static final String STYLE_TOOLBAR_ACTIVE = "active";
	public static final String STYLE_TOOLBAR_COUNT = "toolbar-count";
	public static final String STYLE_TOOLBAR_POPUP = "toolbar-popup";
	public static final String STYLE_DRAGGABLE_LAYOUT = "v-modif";

	// Accepted file types
	public static final String MIMETYPE_MP3 = "audio/mp3";
	public static final List<String> ACCEPTED_AUDIO_MIMETYPES = Arrays.asList(MIMETYPE_MP3);

	public static final String MIMETYPE_BPM = "image/bpm";
	public static final String MIMETYPE_GIF = "image/gif";
	public static final String MIMETYPE_JPEG = "image/jpeg";
	public static final String MIMETYPE_PNG = "image/png";
	public static final List<String> ACCEPTED_IMAGE_MIMETYPES = Arrays.asList(MIMETYPE_BPM, MIMETYPE_GIF, MIMETYPE_JPEG, MIMETYPE_PNG);
	public static final String MIMETYPE_FLASH_VIDEO = "application/x-shockwave-flash";
	public static final String YOUTUBE_PROTOCOL = "https://youtube.googleapis.com/v/";

	// Locales
	public static final Locale LOCALE_TURKISH = new Locale("tr", "TR");
	public static final Locale LOCALE_FRENCH = Locale.FRENCH;

	// Field names
	public static final String QUESTION_TEXT = "QUESTION_TEXT";
	public static final String QUESTION_CHOICE = "QUESTION_CHOICE";
	public static final String QUESTION_IMAGE = "QUESTION_IMAGE";
	public static final String QUESTION_SOUND = "QUESTION_SOUND";
	public static final String QUESTION_VIDEO = "QUESTION_VIDEO";

	// Messages
	public static final String MESSAGE_SUCCESFULLY_SAVED = "message.successfully.saved";
	public static final String MESSAGE_SUCCESFULLY_DELETED = "message.successfully.deleted";
	public static final String MESSAGE_CONFIRMATION_DIALOG = "message.confirmation.dialog";
	public static final String MESSAGE_WELCOME = "message.welcome";
	public static final String MESSAGE_IS_NOT_VALID = "message.not.valid";
	public static final String MESSAGE_DISCARD_CHANGES = "message.discard.changes";
	public static final String MESSAGE_WAIT = "message.wait";
	public static final String MESSAGE_DROP_FILE = "message.dropFileHere";
	public static final String MESSAGE_FILE_MISMATCH = "message.fileMismatch";
	public static final String MESSAGE_INTERRUPT = "message.interrupt";
	public static final String MESSAGE_SELECT = "message.select";
	public static final String MESSAGE_OR = "message.or";

	public static final String BUTTON_SAVE = "button.save";
	public static final String BUTTON_DELETE = "button.delete";
	public static final String BUTTON_CANCEL = "button.cancel";
	public static final String BUTTON_YES = "button.yes";
	public static final String BUTTON_NO = "button.no";
	public static final String BUTTON_EDIT = "button.edit";
	public static final String BUTTON_NEW = "button.new";
	public static final String BUTTON_OK = "button.ok";
	public static final String BUTTON_APPLY = "button.apply";
	public static final String BUTTON_PREVIOUS = "button.previous";
	public static final String BUTTON_NEXT = "button.next";
	public static final String BUTTON_TR = "button.language.tr";
	public static final String BUTTON_FR = "button.language.fr";

	public static final String ACTION_ADD_TEXT = "action.add.text";
	public static final String ACTION_ADD_ANSWER = "action.add.answer";
	public static final String ACTION_ADD_IMAGE = "action.add.image";
	public static final String ACTION_ADD_SOUND = "action.add.sound";
	public static final String ACTION_ADD_VIDEO = "action.add.video";
	public static final String ACTION_REMOVE = "action.remove";
	public static final String ACTION_RESIZE = "action.resize";
	public static final String ACTION_DETAIL = "action.details";

	public static final String LOGIN_FORM_CAPTION = "login.form.caption";
	public static final String LOGIN_USERNAME = "login.form.username";
	public static final String LOGIN_PASSWORD = "login.form.password";
	public static final String LOGIN_ERROR = "login.form.error";
	public static final String LOGIN_BUTTON = "login.button";

	public static final String MENU_EXAM = "left.menu.exam";
	public static final String MENU_QUESTION = "left.menu.question";
	public static final String MENU_STUDENT = "left.menu.student";
	public static final String MENU_MESSAGE = "left.menu.message";
	public static final String MENU_PROFILE = "left.menu.profile";
	public static final String MENU_LOGOUT = "left.menu.logout";

	public static final String EXAM_NAME = "exam.name";
	public static final String EXAM_DESCRIPTION = "exam.description";
	public static final String EXAM_START = "exam.start";
	public static final String EXAM_END = "exam.end";
	public static final String EXAM_QUESTIONS_EXIST = "exam.questions.exist";
	public static final String EXAM_QUESTIONS_ADDED = "exam.questions.add";
	public static final String EXAM_RESULT = "exam.result";

	public static final String QUESTION_INFORMATION = "question.information";
	public static final String QUESTION_CONTENT = "question.content";
	public static final String QUESTION_NAME = "question.name";
	public static final String QUESTION_TIME = "question.time";
	public static final String QUESTION_POINT = "question.point";
	public static final String QUESTION_CREATOR = "question.creator";
	public static final String QUESTION_FIELDS = "question.fields";
	public static final String QUESTION_FIELD_MESSAGE = "question.field.message";
	public static final String QUESTION_DELETION_ERROR = "question.deletion.error";
	public static final String QUESTION_TRUE = "question.true";
	public static final String QUESTION_FALSE = "question.false";
	public static final String QUESTION_EMPTY = "question.empty";

	public static final String MONTHLY = "monthly";
	public static final String WEEKLY = "weekly";
	public static final String DAILY = "daily";

	public static final String DASHBOARD = "dashboard";
	public static final String DASHBOARD_EXAM_CREATION_RATE = "dashboard.exam.creation.rate";
	public static final String DASHBOARD_QUESTION_CREATION_RATE = "dashboard.question.creation.rate";
	public static final String DASHBOARD_TEACHERS = "dashboard.teachers";
	public static final String DASHBOARD_QUESTION_NUMBER = "dashboard.question.number";

	public static final String USER_FIRST_NAME = "user.first.name";
	public static final String USER_LAST_NAME = "user.last.name";
	public static final String USER_EMAIL = "user.email";
	public static final String USER_FACULTY = "user.faculty";
	public static final String USER_PHONE = "user.phone";
	public static final String USER_ACADEMIC_RANK = "user.academic.rank";
	public static final String USER_CLASS = "user.class";
}
