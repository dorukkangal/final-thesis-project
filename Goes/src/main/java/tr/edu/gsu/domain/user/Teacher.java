package tr.edu.gsu.domain.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import tr.edu.gsu.domain.exam.Exam;
import tr.edu.gsu.domain.exam.Question;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Teacher extends GoesUser {
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "creator", cascade = CascadeType.ALL)
	private Set<Exam> exams = new HashSet<Exam>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "creator", cascade = CascadeType.ALL)
	private Set<Question> questions = new HashSet<Question>();

	public Teacher(String email, String password, String firstName, String lastName, byte[] picture, Department department) {
		super(email, password, firstName, lastName, picture, department);
	}

	public Teacher() {

	}

	public Set<Exam> getExams() {
		return exams;
	}

	public void setExams(Set<Exam> exams) {
		this.exams = exams;
	}

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}
}
