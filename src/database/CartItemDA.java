package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;

public class CartItemDA {

	private Connect connectionManager = Connect.getInstance();

	// === Method Insert/Upsert (Sesuai Diagram 3) ===
	public boolean saveDA(String idCustomer, String idProduct, int count) {
		Connection conn = connectionManager.getConnection();
		boolean isSuccess = false;
		try {
			// Logic: Insert or Update on Duplicate
			String query = "INSERT INTO cart_items (id_customer, id_product, count) VALUES (?, ?, ?) "
					+ "ON DUPLICATE KEY UPDATE count = count + ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, idCustomer);
			ps.setString(2, idProduct);
			ps.setInt(3, count);
			ps.setInt(4, count);
			if (ps.executeUpdate() > 0)
				isSuccess = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	// === Method Update Khusus Edit (Sesuai Diagram 4: saveDA context update) ===
	// Digunakan saat user menekan tombol "Update" di CartItemWindow
	public boolean updateCartItemAmount(String idCustomer, String idProduct, int newCount) {
		Connection conn = connectionManager.getConnection();
		try {
			String query = "UPDATE cart_items SET count = ? WHERE id_customer = ? AND id_product = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, newCount);
			ps.setString(2, idCustomer);
			ps.setString(3, idProduct);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// === Method Get List (Untuk Activity Diagram: Display cart item list) ===
	public List<CartItem> getCartItems(String idCustomer) {
		List<CartItem> list = new ArrayList<>();
		Connection conn = connectionManager.getConnection();
		try {
			// Join dengan tabel products untuk ambil nama & harga
			String query = "SELECT c.id_customer, c.id_product, c.count, p.name, p.price "
					+ "FROM cart_items c JOIN products p ON c.id_product = p.id_product " + "WHERE c.id_customer = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, idCustomer);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new CartItem(rs.getString("id_customer"), rs.getString("id_product"), rs.getString("name"),
						rs.getDouble("price"), rs.getInt("count")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// === BARU: Method Delete sesuai Sequence Diagram 5 ===
    public boolean deleteCartItem(String idCustomer, String idProduct) {
        Connection conn = connectionManager.getConnection();
        boolean isSuccess = false;
        try {
            String query = "DELETE FROM cart_items WHERE id_customer = ? AND id_product = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idCustomer);
            ps.setString(2, idProduct);
            
            int result = ps.executeUpdate();
            if (result > 0) isSuccess = true;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}