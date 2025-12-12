package database;

import java.sql.*;

public class OrderHeaderDA {

    // Method utama untuk menyimpan Header
    public String saveOrderHeader(String idCustomer, String promoCode, double totalAmount) {
        
        // 1. Generate ID Baru secara manual (Logic Biasa)
        String newOrderId = generateOrderId(); 
        
        // 2. Query Insert
        String sql = "INSERT INTO order_header (id_order, id_customer, promo_code, total_amount, transaction_date) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nama_db", "user", "pass");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newOrderId);
            pstmt.setString(2, idCustomer);
            
            // Handle jika promoCode null
            if (promoCode == null || promoCode.isEmpty()) {
                pstmt.setNull(3, Types.VARCHAR);
            } else {
                pstmt.setString(3, promoCode);
            }
            
            pstmt.setDouble(4, totalAmount);

            pstmt.executeUpdate();
            
            // PENTING: Kembalikan ID baru agar bisa dipakai oleh OrderDetail
            return newOrderId; 

        } catch (SQLException e) {
            System.out.println("Error saveOrderHeader: " + e.getMessage());
            return null;
        }
    }

    // --- Helper Method: Logic Generasi ID Berurutan ---
    private String generateOrderId() {
        String newId = "ORD-001"; // Default jika tabel kosong
        
        // Ambil ID terbesar/terakhir yang ada di tabel
        String sql = "SELECT id_order FROM order_header ORDER BY id_order DESC LIMIT 1";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nama_db", "user", "pass");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                // Misal ID terakhir: "ORD-005"
                String lastId = rs.getString("id_order");
                int number = Integer.parseInt(lastId.substring(4));
                number++;
                newId = String.format("ORD-%03d", number);
            }

        } catch (SQLException e) {
            System.out.println("Error generateOrderId: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing ID: " + e.getMessage());
            // Fallback jika format ID di database tidak sesuai standar "ORD-XXX"
        }
        
        return newId;
    }
}