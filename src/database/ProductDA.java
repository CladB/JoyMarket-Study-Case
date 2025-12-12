package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductDA {

	private Connect connectionManager = Connect.getInstance();

	// === Method read() sesuai Sequence Diagram (1.1.1.1) ===
	public List<Product> read() {
		List<Product> productList = new ArrayList<>();
		String query = "SELECT * FROM products";

		ResultSet rs = connectionManager.execQuery(query);

		try {
			while (rs.next()) {
				Product p = new Product(rs.getString("id_product"), rs.getString("name"),
						rs.getBigDecimal("price").doubleValue(), rs.getInt("stock"), rs.getString("category"));
				productList.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productList;
	}

	public Product getProduct(String idProduct) {
		Product product = null;

		// Query contoh (Sesuaikan nama tabel dan kolom dengan DB Anda)
		String sql = "SELECT * FROM product WHERE id_product = ?";

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nama_db", "user", "pass");
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, idProduct);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					// Mapping dari Database ke Model Product
					product = new Product();
					product.setIdProduct(rs.getString("id_product"));
					product.setName(rs.getString("name"));
					product.setPrice(rs.getDouble("price"));
					product.setStock(rs.getInt("stock"));
					// product.setCategory(rs.getString("category"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error getProduct: " + e.getMessage());
		}

		return product;
	}

	public void decreaseStock(String idProduct, int quantity) {
		String sql = "UPDATE product SET stock = stock - ? WHERE id_product = ?";

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nama_db", "user", "pass");
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, quantity);
			pstmt.setString(2, idProduct);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error decreaseStock: " + e.getMessage());
		}
	}
	
}