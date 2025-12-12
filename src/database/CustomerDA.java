package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDA {

	private Connect connectionManager = Connect.getInstance();

	// === Update Saldo ===
	public boolean saveDA(String idCustomer, double amount) {
		Connection conn = connectionManager.getConnection();
		boolean isSuccess = false;

		try {
			// === tambah saldo lama dengan yang baru ===
			String query = "UPDATE customers SET balance = balance + ? WHERE id_customer = ?";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setDouble(1, amount);
			ps.setString(2, idCustomer);

			int result = ps.executeUpdate();
			if (result > 0)
				isSuccess = true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isSuccess;
	}

	// === Show Ambil saldo saat ini ===
	public double getCurrentBalance(String idCustomer) {
		double balance = 0.0;
		try {
			String query = "SELECT balance FROM customers WHERE id_customer = '" + idCustomer + "'";
			ResultSet rs = connectionManager.execQuery(query);
			if (rs.next()) {
				balance = rs.getDouble("balance");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return balance;
	}

	public void updateBalance(String idCustomer, double newBalance) {
		String query = "UPDATE customers SET balance = ? WHERE id_customer = ?";
		
		Connection conn = connectionManager.getConnection();
		
		try (PreparedStatement ps = conn.prepareStatement(query)) {
	        ps.setDouble(1, newBalance);
	        ps.setString(2, idCustomer);
	        ps.executeUpdate();

	    } catch (SQLException e) {
	        System.out.println("Error updateBalance: " + e.getMessage());
	        throw new RuntimeException(e);
	    }

	}
}