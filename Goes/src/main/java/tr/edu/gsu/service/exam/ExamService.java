package tr.edu.gsu.service.exam;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.service.BaseService;

public interface ExamService extends BaseService<Exam> {

	public long countCreatedExam(Teacher teacher);
}
