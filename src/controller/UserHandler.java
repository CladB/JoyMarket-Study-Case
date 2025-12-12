package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import database.Connect;
import database.UserDA;
import model.Customer;
import model.User;

public class UserHandler {

	// === Instance Data Access dan connect database ===
	private UserDA userDA = new UserDA();
	private Connect con = Connect.getInstance();

	// === REGISTER ===
	public String saveDataUser(String fullName, String email, String password, String gender, String phone,
			String address) {

		// === Validasi Input ===
		if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty() || phone.isEmpty()
				|| address.isEmpty()) {
			return "Validasi Gagal: Semua kolom harus diisi!";
		}

		if (userDA.isEmailExists(email)) {
			return "Validasi Gagal: Email sudah terdaftar!";
		}

		if (password.length() < 6) {
			return "Validasi Gagal: Password minimal 6 karakter!";
		}

		if (phone.length() < 10 || phone.length() > 13) {
			return "Validasi Gagal: Nomor Telepon harus 10-13 digit!";
		}

		if (!phone.matches("\\d+")) {
			return "Validasi Gagal: Nomor Telepon harus berupa angka!";
		}

		// === Create Object Customer ===
		String newId = userDA.generateNextId();

		Customer newCustomer = new Customer(newId, fullName, email, password, gender, phone, address, "Customer", 0.0);

		// === Save into Database (saveDA )===
		boolean isSuccess = userDA.saveDA(newCustomer);

		if (isSuccess) {
			return "Success";
		} else {
			return "Error";
		}
	}

	// === LOGIN ===
	public User login(String email, String password) {
		User user = null;
		try {
			String query = String.format("SELECT * FROM users WHERE email = '%s' AND password = '%s'", email, password);
			ResultSet rs = con.execQuery(query);

			if (rs.next()) {
				// === mapping data dari SQL ke Object User ===
				user = new User(rs.getString("id_user"), 
						rs.getString("full_name"), 
						rs.getString("email"),
						rs.getString("password"), 
						rs.getString("email"), 
						rs.getString("phone"), 
						rs.getString("address"),
						rs.getString("role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user; // Mengembalikan object User ketika berhasil
	}

	public User getUser(String idUser) {
		return userDA.getUser(idUser);
	}

	// === Edit Profile ===
	public String editProfile(String idUser, String name, String email, String password, String phone, String address) {

		// === Validasi Input yang Kosong ===
		if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
			return "Error: Semua kolom harus diisi.";
		}

		// === Validasi format nomor/ phone ===
		if (!phone.matches("\\d+") || phone.length() < 10 || phone.length() > 13) {
			return "Error: Nomor telepon tidak valid (10-13 digit angka).";
		}

		// === Validasi Password ===
		if (password.length() < 6) {
			return "Error: Password minimal 6 karakter.";
		}

		User oldData = userDA.getUser(idUser);
		if (oldData == null)
			return "Error: User tidak ditemukan.";

		// === Buat Object User Baru ===
		User updatedUser = new User(idUser, name, email, password, oldData.getGender(), phone, address, oldData.getRole());

		// === Save ke Database ===
		if (userDA.updateUser(updatedUser)) {
			return "Success";
		} else {
			return "Error: Gagal mengupdate data.";
		}
	}
}