package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Delivery;

public class DeliveryDA {

	private Connect connectionManager = Connect.getInstance();

	public boolean saveDelivery(Delivery delivery) {
		String query = "INSERT INTO deliveries (id_order, id_courier, status) VALUES (?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE id_courier = ?, status = ?";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			// === INSERT===
			ps.setString(1, delivery.getIdOrder());
			ps.setString(2, delivery.getIdCourier());
			ps.setString(3, delivery.getStatus());

			// === UPDATE ===
			ps.setString(4, delivery.getIdCourier()); // kalau dpouble akan di replace dengan kurir baru
			ps.setString(5, delivery.getStatus());

			int result = ps.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			System.out.println("Error saveDelivery: " + e.getMessage());
			return false;
		}
	}

	// === Update Status Pengiriman ===
	public boolean updateStatus(String idOrder, String newStatus) {
		String query = "UPDATE deliveries SET status = ? WHERE id_order = ?";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, newStatus);
			ps.setString(2, idOrder);

			int result = ps.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			System.out.println("Error DeliveryDA updateStatus: " + e.getMessage());
			return false;
		}
	}
}