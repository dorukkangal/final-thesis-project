package tr.edu.gsu.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import tr.edu.gsu.GoesApplication;
import tr.edu.gsu.service.exam.CourseService;
import tr.edu.gsu.service.exam.ExamService;
import tr.edu.gsu.service.exam.QuestionFieldService;
import tr.edu.gsu.service.exam.QuestionService;
import tr.edu.gsu.service.message.MessageService;
import tr.edu.gsu.service.user.StudentService;
import tr.edu.gsu.service.user.TeacherService;
import tr.edu.gsu.session.GoesSession;
import tr.edu.gsu.view.MainWindow;

public class AppContext implements ApplicationContextAware {

	private static ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		AppContext.ctx = ctx;
	}

	public static Object getBean(String beanName) {
		return ctx.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		return ctx.getBean(beanName, clazz);
	}

	public static <T> T getBean(Class<T> clazz) {
		return ctx.getBean(clazz);
	}

	public static TeacherService getTeacherService() {
		return getBean("teacherService", TeacherService.class);
	}

	public static StudentService getStudentService() {
		return getBean("studentService", StudentService.class);
	}

	public static ExamService getExamService() {
		return getBean("examService", ExamService.class);
	}
	
	public static CourseService getCourseService() {
		return getBean("courseService", CourseService.class);
	}

	public static QuestionService getQuestionService() {
		return getBean("questionService", QuestionService.class);
	}

	public static QuestionFieldService getQuestionFieldService() {
		return getBean("questionFieldService", QuestionFieldService.class);
	}

	public static MessageService getMessageService() {
		return getBean("messageService", MessageService.class);
	}

	public static GoesSession getSession() {
		return getBean("session", GoesSession.class);
	}

	public static MainWindow getMainWindow() {
		return getBean("mainWindow", MainWindow.class);
	}

	public static GoesApplication getApp() {
		return getBean("goesApplication", GoesApplication.class);
	}
}