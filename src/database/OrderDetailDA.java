package database;

import java.sql.*;

public class OrderDetailDA {

	private Connect connectionManager = Connect.getInstance();

	public void saveDetail(String idOrder, String idProduct, int quantity) {
		String query = "INSERT INTO order_details (id_order, id_product, qty) VALUES (?, ?, ?)";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, idOrder);
			ps.setString(2, idProduct);
			ps.setInt(3, quantity);
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error saveDetail: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}