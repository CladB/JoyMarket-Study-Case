package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDA {

	private Connect connectionManager = Connect.getInstance();

	// === Method UPDATE Saldo (Sequence: 1.1.1.1: saveDA) ===
	public boolean saveDA(String idCustomer, double amount) {
		Connection conn = connectionManager.getConnection();
		boolean isSuccess = false;

		try {
			// Query: Tambahkan saldo lama dengan amount baru
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

	// Helper: Ambil saldo saat ini (Untuk ditampilkan di View)
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
		String sql = "UPDATE customer SET balance = ? WHERE id_customer = ?";

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nama_db", "user", "pass");
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setDouble(1, newBalance);
			pstmt.setString(2, idCustomer);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error updateBalance: " + e.getMessage());
			// Opsional: Throw exception agar Controller tahu update gagal
			throw new RuntimeException(e);
		}
	}
}