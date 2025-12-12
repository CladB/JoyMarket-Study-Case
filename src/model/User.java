package model;

public class User {
	protected String idUser;
	protected String fullName;
	protected String email;
	protected String password;
	protected String gender;
	protected String phone;
	protected String address;
	protected String role;

	public User(String idUser, String fullName, String email, String password, String gender, String phone,
			String address, String role) {
		this.idUser = idUser;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.role = role;
	}

	public String getIdUser() {
		return idUser;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getGender() {
		return gender;
	}

	public String getRole() {
		return role;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}
}