package tr.edu.gsu.service.exam;

import tr.edu.gsu.domain.exam.Course;
import tr.edu.gsu.service.BaseServiceImpl;

public class CourseServiceImpl extends BaseServiceImpl<Course> implements CourseService {
	private static final long serialVersionUID = 1L;

	public CourseServiceImpl() {
		super(Course.class);
	}
}