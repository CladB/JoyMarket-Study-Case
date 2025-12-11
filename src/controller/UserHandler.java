package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import database.Connect;
import database.UserDA;
import model.Customer;
import model.User;

public class UserHandler {

	// Instance Data Access & Koneksi
	private UserDA userDA = new UserDA();
	private Connect con = Connect.getInstance();
	
	// METHOD 1: REGISTER (Sequence Diagram 1)
	public String saveDataUser(String fullName, String email, String password, String phone, String address) {

		// 1. Validasi Input
		if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
			return "Validasi Gagal: Semua kolom harus diisi!";
		}

		if (userDA.isEmailExists(email)) {
			return "Validasi Gagal: Email sudah terdaftar!";
		}

		if (password.length() < 6) {
			return "Validasi Gagal: Password minimal 6 karakter!";
		}

		// 2. Buat Object Customer
		String newId = userDA.generateNextId();

		Customer newCustomer = new Customer(newId, fullName, email, password, phone, address, "Customer", 0.0);

		// 3. Simpan ke Database
		boolean isSuccess = userDA.saveDA(newCustomer);

		if (isSuccess) {
			return "Success";
		} else {
			return "Error";
		}
	}

	// ==========================================
	// METHOD 2: LOGIN (Diperlukan untuk Sequence 3)
	// ==========================================
	public User login(String email, String password) {
		User user = null;
		try {
			// Query sederhana untuk mencari user berdasarkan email & password
			String query = String.format("SELECT * FROM users WHERE email = '%s' AND password = '%s'", email, password);
			ResultSet rs = con.execQuery(query);

			if (rs.next()) {
				// Jika ditemukan, mapping data dari SQL ke Object User
				user = new User(rs.getString("id_user"), rs.getString("full_name"), rs.getString("email"),
						rs.getString("password"), rs.getString("phone"), rs.getString("address"), rs.getString("role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user; // Mengembalikan object User jika sukses, atau null jika gagal
	}
}