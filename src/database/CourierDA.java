package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Courier;

public class CourierDA {

	private Connect connectionManager = Connect.getInstance();

	public List<Courier> read() {
		List<Courier> courierList = new ArrayList<>();

		String query = "SELECT c.id_courier, u.id_user, u.full_name, u.email, u.phone, u.address, "
				+ "c.vehicle_type, c.vehicle_plate " 
				+ "FROM couriers c " 
				+ "JOIN users u ON c.id_courier = u.id_user";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Courier c = new Courier();
				
				c.setIdCourier(rs.getString("id_courier"));
				c.setIdUser(rs.getString("id_user")); 

				// === Tabel Users ===
				c.setName(rs.getString("full_name")); 
				c.setEmail(rs.getString("email"));
				c.setPhone(rs.getString("phone"));
				c.setAddress(rs.getString("address"));

				// === Tabel Couriers ===
				c.setVehicleType(rs.getString("vehicle_type"));
				c.setPlateNumber(rs.getString("vehicle_plate"));

				courierList.add(c);
			}

		} catch (SQLException e) {
			System.out.println("Error CourierDA read: " + e.getMessage());
		}
		return courierList;
	}
}