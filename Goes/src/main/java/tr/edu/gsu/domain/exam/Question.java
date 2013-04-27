package tr.edu.gsu.domain.exam;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import tr.edu.gsu.domain.BaseEntity;
import tr.edu.gsu.domain.user.Teacher;

@Entity
public class Question extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String name;

	private Time time;

	private int point;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<QuestionField> fields = new HashSet<QuestionField>();

	@ManyToOne(fetch = FetchType.LAZY)
	private Teacher creator;

	public Question(String name, Time time, Set<QuestionField> fields, Teacher creator) {
		super();
		this.name = name;
		this.time = time;
		this.fields = fields;
		this.creator = creator;
	}

	public Question() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public Set<QuestionField> getFields() {
		return fields;
	}

	public void setFields(Set<QuestionField> fields) {
		this.fields = fields;
	}

	public Teacher getCreator() {
		return creator;
	}

	public void setCreator(Teacher creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return name;
	}
}
