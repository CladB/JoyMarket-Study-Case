package controller;

import database.CustomerDA;

public class CustomerHandler {

	CustomerDA customerDA = new CustomerDA();

	// === TOP UP BALANCE ===
	public String topUpBalance(String idCustomer, double amount) {
		// Validasi Top Up
		if (amount <= 0) {
			return "Error: Jumlah Top Up tidak bisa kurang dari 0.";
		} else if (amount < 10000) {
			return "Error: Jumlah Top Up minimal 10.000";
		}

		// === Simpan ke Database ===
		boolean isSuccess = customerDA.saveDA(idCustomer, amount);

		if (isSuccess) {
			return "Success";
		} else {
			return "Error: Gagal melakukan Top Up.";
		}
	}

	// === Helper untuk View ===
	public double getBalance(String idCustomer) {
		return customerDA.getCurrentBalance(idCustomer);
	}
}