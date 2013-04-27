package tr.edu.gsu.service.exam;

import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.service.BaseService;

public interface QuestionService extends BaseService<Question> {

	public long countCreatedQuestion(Teacher teacher);
}
