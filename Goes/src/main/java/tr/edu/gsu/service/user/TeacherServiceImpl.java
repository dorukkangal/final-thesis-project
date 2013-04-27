package tr.edu.gsu.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tr.edu.gsu.domain.user.Teacher;

@Service
@Transactional
public class TeacherServiceImpl extends GoesUserServiceImpl<Teacher> implements TeacherService {
	private static final long serialVersionUID = 1L;

	public TeacherServiceImpl() {
		super(Teacher.class);
	}
}
