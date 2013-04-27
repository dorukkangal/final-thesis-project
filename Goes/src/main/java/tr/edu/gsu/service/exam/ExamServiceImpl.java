package tr.edu.gsu.service.exam;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.user.Teacher;
import tr.edu.gsu.service.BaseServiceImpl;

public class ExamServiceImpl extends BaseServiceImpl<Exam> implements ExamService {
	private static final long serialVersionUID = 1L;

	public ExamServiceImpl() {
		super(Exam.class);
	}

	@Override
	public long countCreatedExam(Teacher teacher) {
		if (teacher != null)
			return em.createQuery("SELECT COUNT(o) FROM Exam o WHERE o.creator = ?1", Long.class).setParameter(1, teacher).getSingleResult();
		else
			return em.createQuery("SELECT COUNT(o) FROM Exam o", Long.class).getSingleResult();
	}
}