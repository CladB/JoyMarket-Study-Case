package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductDA {

	private Connect connectionManager = Connect.getInstance();

	// === Read ===
	public List<Product> read() {
		List<Product> productList = new ArrayList<>();
		String query = "SELECT * FROM products";
		ResultSet rs = connectionManager.execQuery(query);
		try {
			while (rs.next()) {
				Product p = new Product(rs.getString("id_product"), 
						rs.getString("name"),
						rs.getBigDecimal("price").doubleValue(), 
						rs.getInt("stock"),
						rs.getString("category"));
				productList.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}

	//	=== Get Product ===
	public Product getProduct(String idProduct) {
		Product product = null;
		String query = "SELECT * FROM products WHERE id_product = ?";
		Connection conn = connectionManager.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, idProduct);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					product = new Product();
					product.setIdProduct(rs.getString("id_product"));
					product.setName(rs.getString("name"));
					product.setPrice(rs.getDouble("price"));
					product.setStock(rs.getInt("stock"));
					product.setCategory(rs.getString("category"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error getProduct: " + e.getMessage());
		}
		return product;
	}

	// === Decrease Stock ===
	public void decreaseStock(String idProduct, int quantity) {
		String query = "UPDATE products SET stock = stock - ? WHERE id_product = ?";
		Connection conn = connectionManager.getConnection();
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, quantity);
			ps.setString(2, idProduct);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error decreaseStock: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// === Update Product Stock ===
	public boolean updateProductStock(String idProduct, int newStock) {
		String query = "UPDATE products SET stock = ? WHERE id_product = ?";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, newStock);
			ps.setString(2, idProduct);

			int rowsUpdated = ps.executeUpdate();
			return rowsUpdated > 0;

		} catch (SQLException e) {
			System.out.println("Error updateProductStock: " + e.getMessage());
			return false;
		}
	}
}