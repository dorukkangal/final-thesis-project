package tr.edu.gsu.domain.exam;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import tr.edu.gsu.domain.BaseEntity;
import tr.edu.gsu.domain.user.Teacher;

@Entity
public class Exam extends BaseEntity implements Comparable<Exam> {
	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date start;

	private Date end;

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Question> questions = new HashSet<Question>();

	@ManyToOne(fetch = FetchType.LAZY)
	private Teacher creator;

	public Exam(String name, String description, Date start, Date end, Set<Question> questions, Teacher creator) {
		super();
		this.name = name;
		this.description = description;
		this.start = start;
		this.end = end;
		this.questions = questions;
		this.creator = creator;
	}

	public Exam() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Teacher getCreator() {
		return creator;
	}

	public void setCreator(Teacher creator) {
		this.creator = creator;
	}

	@Override
	public int compareTo(Exam o) {
		return start.compareTo(o.getStart());
	}

	@Override
	public String toString() {
		return name;
	}
}
