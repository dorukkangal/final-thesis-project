package tr.edu.gsu.service.exam;

import tr.edu.gsu.domain.exam.Question;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.service.BaseServiceImpl;

public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService {
	private static final long serialVersionUID = 1L;

	public QuestionServiceImpl() {
		super(Question.class);
	}

	@Override
	public long countCreatedQuestion(Teacher teacher) {
		if (teacher != null)
			return em.createQuery("SELECT COUNT(o) FROM Question o WHERE o.creator = ?1", Long.class).setParameter(1, teacher).getSingleResult();
		else
			return em.createQuery("SELECT COUNT(o) FROM Question o", Long.class).getSingleResult();
	}
}