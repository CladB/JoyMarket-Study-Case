package model;

public class Admin extends User {
	private String emergencyContact;

	// === Constructor Template (flexible) ===
	public Admin() {
		super("", "", "", "", "-", "", "", "Admin");
	}

	// === Constructor Lengkap ===
	public Admin(String idUser, String fullName, String email, String password, String gender, String phone,
			String address, String emergencyContact) {
		super(idUser, fullName, email, password, gender, phone, address, "Admin");
		this.emergencyContact = emergencyContact;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
}