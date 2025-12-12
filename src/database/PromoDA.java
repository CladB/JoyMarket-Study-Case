package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Promo;

public class PromoDA {
    
    private Connect connectionManager = Connect.getInstance();

    // === Ambil Promo Code ===
    public Promo getPromoByCode(String codeInput) {
        Promo promo = null;
        
        String query = "SELECT id_promo, discount_percentage FROM promos WHERE code = ?";

        Connection conn = connectionManager.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
        	ps.setString(1, codeInput);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                promo = new Promo(
                    rs.getString("id_promo"),
                    rs.getDouble("discount_percentage")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getPromo: " + e.getMessage());
        }
        return promo;
    }
}