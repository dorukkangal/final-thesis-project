package tr.edu.gsu.demo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.domain.exam.QuestionField;
import tr.edu.gsu.domain.user.Student;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.service.exam.ExamService;
import tr.edu.gsu.service.exam.QuestionFieldService;
import tr.edu.gsu.service.exam.QuestionService;
import tr.edu.gsu.service.user.StudentService;
import tr.edu.gsu.service.user.TeacherService;
import tr.edu.gsu.util.AppContext;
import tr.edu.gsu.util.GoesConstants;

public class DemoCreator {

	private static TeacherService teacherService;
	private static StudentService studentService;
	private static ExamService examService;
	private static QuestionService questionService;
	private static QuestionFieldService questionFieldService;

	private static final String PICTURE_FILE_NAME = "demo/avatar.png";
	private static final String IMAGE_FILE_NAME = "demo/flower.jpg";
	private static final String SOUND_FILE_NAME = "demo/intro.mp3";

	private static byte[] picture;
	private static byte[] image;
	private static byte[] sound;

	public static void createDemo() {
		initServices();
		truncateTables();
		initFiles();

		Student student = null;
		// Department department = new Department("Bilgisayar Muhendisligi");
		student = new Student("utkuozdemir@gmail.com", "aaa", "Utku", "Ozdemir", picture, null);
		studentService.update(student);
		student = new Student("adnanbal@gmail.com", "aaa", "Adnan", "Bal", picture, null);
		studentService.update(student);
		student = new Student("berkeoz@gmail.com", "aaa", "Berke", "Oz", picture, null);
		studentService.update(student);

		Teacher teacher = null;
		teacher = new Teacher("burak.arslanl@gsu.edu.tr", "111", "Burak", "Arslan", picture, null);
		teacherService.update(teacher);
		teacher = new Teacher("atay.ozgovde@gsu.edu.tr", "111", "Atay", "Ozgovde", picture, null);
		teacherService.update(teacher);
		teacher = new Teacher("dorukkangal@gmail.com", "drkkng", "Doruk", "Kangal", picture, null);
		teacher = teacherService.update(teacher);
		AppContext.getSession().setUser(teacher);

		Set<Question> questions = new HashSet<Question>();
		for (int i = 1; i <= 4; i++) {
			Question question = new Question("question" + i, Time.valueOf("10:00:00"), new HashSet<QuestionField>(), teacher);

			QuestionField choiceField = new QuestionField(GoesConstants.QUESTION_CHOICE, "bu bir secenek".getBytes(), true, null, "10", "10", "100", "100");
			question.getFields().add(choiceField);
			QuestionField imageField = new QuestionField(GoesConstants.QUESTION_IMAGE, image, null, GoesConstants.MIMETYPE_PNG, "120", "10", "100", "100");
			question.getFields().add(imageField);
			QuestionField soundField = new QuestionField(GoesConstants.QUESTION_SOUND, sound, null, GoesConstants.MIMETYPE_MP3, "120", "120", "100", "100");
			question.getFields().add(soundField);
			questionService.save(question);
			questions.add(question);
		}

		for (int i = 1; i <= 4; i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, (i * 6 - 8));
			Date start = cal.getTime();
			cal.add(Calendar.HOUR, 2);
			Date end = cal.getTime();
			Exam exam = new Exam("exam" + i, "exam description", start, end, questions, teacher);
			examService.save(exam);
		}
	}

	private static void truncateTables() {
		for (Teacher teacher : teacherService.findAll())
			teacherService.delete(teacher);
		for (Student student : studentService.findAll())
			studentService.delete(student);
		for (QuestionField questionField : questionFieldService.findAll())
			questionFieldService.delete(questionField);
		for (Exam exam : examService.findAll()) {
			exam.setQuestions(new HashSet<Question>());
			examService.update(exam);
			examService.delete(exam);
		}
		for (Question question : questionService.findAll())
			questionService.delete(question);
	}

	private static void initServices() {
		teacherService = AppContext.getTeacherService();
		studentService = AppContext.getStudentService();
		examService = AppContext.getExamService();
		questionService = AppContext.getQuestionService();
		questionFieldService = AppContext.getQuestionFieldService();
	}

	private static void initFiles() {
		try {
			picture = FileUtils.readFileToByteArray(readFileFromResource(PICTURE_FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			image = FileUtils.readFileToByteArray(readFileFromResource(IMAGE_FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			sound = FileUtils.readFileToByteArray(readFileFromResource(SOUND_FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static File readFileFromResource(String fileName) throws IOException {
		URL fileURL = DemoCreator.class.getClassLoader().getResource(fileName);
		if (fileURL == null)
			throw new IOException("Resource not found " + fileName);

		return new File(fileURL.getFile());
	}
}
