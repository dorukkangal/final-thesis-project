package tr.edu.gsu.service.user;

import java.util.List;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.user.Student;
import tr.edu.gsu.domain.user.StudentExam;

public interface StudentService extends GoesUserService<Student> {

	public List<StudentExam> findParticipatedExamResults(Student student, Exam exam);
}
