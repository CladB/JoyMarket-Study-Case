package database;

import java.sql.*;
import model.Promo;

public class PromoDA {

	// Method untuk mengambil data Promo berdasarkan Kode (Sequence: 4.1.1)
	public Promo getPromo(String promoCode) {
		Promo promo = null;
		String sql = "SELECT * FROM promo WHERE code = ?";

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nama_db", "user", "pass");
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, promoCode);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					promo = new Promo();
					promo.setIdPromo(rs.getString("id_promo"));
					promo.setCode(rs.getString("code"));
					promo.setDescription(rs.getString("description"));

					// Asumsi di database tersimpan dalam persen (misal: 10 untuk 10%)
					// Atau nominal (misal: 10000). Sesuaikan dengan tipe data di Model.
					promo.setDiscountPercentage(rs.getDouble("discount_percentage"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error getPromo: " + e.getMessage());
		}

		return promo;
	}
}