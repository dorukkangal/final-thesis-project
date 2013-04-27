package tr.edu.gsu.domain.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Student extends GoesUser {
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
	private Set<StudentExam> participatedExams = new HashSet<StudentExam>();

	public Student(String email, String password, String firstName, String lastName, byte[] picture, Department department) {
		super(email, password, firstName, lastName, picture, department);
	}

	public Student() {

	}

	public Set<StudentExam> getParticipatedExams() {
		return participatedExams;
	}

	public void setParticipatedExams(Set<StudentExam> participatedExams) {
		this.participatedExams = participatedExams;
	}
}
