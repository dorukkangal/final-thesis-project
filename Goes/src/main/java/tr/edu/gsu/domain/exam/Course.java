package tr.edu.gsu.domain.exam;

import javax.persistence.Entity;

import tr.edu.gsu.domain.BaseEntity;

@Entity
public class Course extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String name;

	public Course(String name) {
		super();
		this.name = name;
	}

	public Course() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
