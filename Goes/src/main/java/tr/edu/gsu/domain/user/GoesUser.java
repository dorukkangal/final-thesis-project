package tr.edu.gsu.domain.user;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import tr.edu.gsu.domain.BaseEntity;

@Entity
public abstract class GoesUser extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String gsuId;

	private String email;

	private String password;

	private String firstName;

	private String lastName;

	private String phoneNumber;

	private String academicRank;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] picture;

	@ManyToOne(fetch = FetchType.LAZY)
	private Department department;

	public GoesUser(String email, String password, String firstName, String lastName, byte[] picture, Department department) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.picture = picture;
		this.department = department;
	}

	public GoesUser() {

	}

	public String getGsuId() {
		return gsuId;
	}

	public void setGsuId(String gsuId) {
		this.gsuId = gsuId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAcademicRank() {
		return academicRank;
	}

	public void setAcademicRank(String academicRank) {
		this.academicRank = academicRank;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Override
	public String toString() {
		return getFullName();
	}
}
