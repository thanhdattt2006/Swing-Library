package entities;

import java.util.Date;

public class Account {
	private int id;
	private String employee_id;
	private String username;
	private String password;
	private String name;
	private int department_id;
	private int role_id;
	private String gender;
	private Date birthday;
	private String photo;
	private String address;
	private String phone;
	private String department_name;

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "account [id=" + id + ", employee_id=" + employee_id + ", username=" + username + ", password="
				+ password + ", name=" + name + ", department_id=" + department_id + ", role_id=" + role_id
				+ ", gender=" + gender + ", birthday=" + birthday + ", photo=" + photo + ", address=" + address
				+ ", phone=" + phone + "]";
	}

	public Account(int id, String employee_id, String username, String password, String name, int department_id,
			int role_id, String gender, Date birthday, String photo, String address, String phone) {
		super();
		this.id = id;
		this.employee_id = employee_id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.department_id = department_id;
		this.role_id = role_id;
		this.gender = gender;
		this.birthday = birthday;
		this.photo = photo;
		this.address = address;
		this.phone = phone;
	}

	public Account() {
		super();
	}

}
