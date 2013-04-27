package tr.edu.gsu.domain.user;

import javax.persistence.Entity;

import tr.edu.gsu.domain.BaseEntity;

@Entity
public class Department extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String name;

	public Department(String name) {
		super();
		this.name = name;
	}

	public Department() {

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
