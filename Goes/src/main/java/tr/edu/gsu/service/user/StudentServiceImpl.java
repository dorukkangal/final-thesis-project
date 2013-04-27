package tr.edu.gsu.service.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.user.Student;
import tr.edu.gsu.domain.user.StudentExam;

@Service
@Transactional
public class StudentServiceImpl extends GoesUserServiceImpl<Student> implements StudentService {
	private static final long serialVersionUID = 1L;

	public StudentServiceImpl() {
		super(Student.class);
	}

	@Override
	public List<StudentExam> findParticipatedExamResults(Student student, Exam exam) {
		try {
			if (student != null && exam != null)
				return em.createQuery("SELECT o FROM StudentExam o WHERE o.student=?1 AND o.exam=?2", StudentExam.class).setParameter(1, student).setParameter(2, exam).getResultList();
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
