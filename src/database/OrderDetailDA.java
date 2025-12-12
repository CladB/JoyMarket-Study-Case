package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDetailDA {
	public void saveDetail(String idOrder, String idProduct, int quantity) {
        // Sesuaikan nama tabel (misal: order_detail) dan kolom
        String sql = "INSERT INTO order_detail (id_order, id_product, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nama_db", "user", "pass");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idOrder);
            pstmt.setString(2, idProduct);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error saveDetail: " + e.getMessage());
        }
    }
}
