package tr.edu.gsu.domain.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import tr.edu.gsu.domain.BaseEntity;
import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.exam.Question;

@Entity
public class StudentExam extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Student student;

	@OneToOne(fetch = FetchType.LAZY)
	private Exam exam;

	@OneToOne(fetch = FetchType.LAZY)
	private Question question;

	private boolean isTrue;

	public StudentExam(Student student, Exam exam, Question question, boolean isTrue) {
		super();
		this.student = student;
		this.exam = exam;
		this.question = question;
		this.isTrue = isTrue;
	}

	public StudentExam() {

	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

	@Override
	public String toString() {
		return student.getFullName() + "|" + exam.getName() + "|" + question.getName();
	}
}
