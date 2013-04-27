package tr.edu.gsu.service.exam;

import tr.edu.gsu.domain.exam.QuestionField;
import tr.edu.gsu.service.BaseServiceImpl;

public class QuestionFieldServiceImpl extends BaseServiceImpl<QuestionField> implements QuestionFieldService {
	private static final long serialVersionUID = 1L;

	public QuestionFieldServiceImpl() {
		super(QuestionField.class);
	}
}