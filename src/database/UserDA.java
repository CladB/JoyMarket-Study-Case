package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Customer;

public class UserDA {
    
    private Connect connectionManager = Connect.getInstance();

    // Generate ID otomatis
    public String generateNextId() {
        String newId = "U001";
        try {
            String query = "SELECT id_user FROM users ORDER BY id_user DESC LIMIT 1";
            ResultSet rs = connectionManager.execQuery(query);
            if (rs.next()) {
                String lastId = rs.getString("id_user");
                int num = Integer.parseInt(lastId.substring(1)) + 1;
                newId = String.format("U%03d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean isEmailExists(String email) {
        try {
            String query = "SELECT email FROM users WHERE email = '" + email + "'";
            ResultSet rs = connectionManager.execQuery(query);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // === REVISI: Nama Method sesuai Sequence Diagram (1.1.1.1: saveDA()) ===
    public boolean saveDA(Customer customer) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement psUser = null;
        PreparedStatement psCust = null;
        boolean isSuccess = false;

        try {
            conn.setAutoCommit(false); // Transaction Start

            // 1. Insert ke Tabel Users
            String sqlUser = "INSERT INTO users (id_user, full_name, email, password, phone, address, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, customer.getIdUser());
            psUser.setString(2, customer.getFullName());
            psUser.setString(3, customer.getEmail());
            psUser.setString(4, customer.getPassword());
            psUser.setString(5, customer.getPhone());
            psUser.setString(6, customer.getAddress());
            psUser.setString(7, "Customer");
            psUser.executeUpdate();

            // 2. Insert ke Tabel Customers
            String sqlCust = "INSERT INTO customers (id_customer, balance) VALUES (?, ?)";
            psCust = conn.prepareStatement(sqlCust);
            psCust.setString(1, customer.getIdUser());
            psCust.setDouble(2, 0.0);
            psCust.executeUpdate();

            conn.commit(); // Transaction Commit
            isSuccess = true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
        return isSuccess;
    }
}