package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Customer;
import model.User;

public class UserDA {
    
    private Connect connectionManager = Connect.getInstance();

    // === Generate ID auto ==-=
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

    // === saveDA customer ===
    public boolean saveDA(Customer customer) {
        Connection conn = connectionManager.getConnection();
        PreparedStatement psUser = null;
        PreparedStatement psCust = null;
        boolean isSuccess = false;

        try {
            conn.setAutoCommit(false);

            // === Insert ke Tabel Users ===
            String sqlUser = "INSERT INTO users (id_user, full_name, email, password, gender, phone, address, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, customer.getIdUser());
            psUser.setString(2, customer.getFullName());
            psUser.setString(3, customer.getEmail());
            psUser.setString(4, customer.getPassword());
            psUser.setString(5, customer.getGender());
            psUser.setString(6, customer.getPhone());
            psUser.setString(7, customer.getAddress());
            psUser.setString(8, "Customer");
            psUser.executeUpdate();

            // === Insert ke Tabel Customers ===
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
    
    public User getUser(String idUser) {
        User user = null;
        String query = "SELECT * FROM users WHERE id_user = ?";
        
        Connection conn = connectionManager.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
        	ps.setString(1, idUser);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new User(
                    rs.getString("id_user"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("role") 
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    
 // === Update Profile ===
    public boolean updateUser(User user) {
        String query = "UPDATE users SET full_name = ?, email = ?, password = ?, phone = ?, address = ? WHERE id_user = ?";
        
        Connection conn = connectionManager.getConnection();
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
        	ps.setString(1, user.getFullName());
        	ps.setString(2, user.getEmail());
        	ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getIdUser());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updateUser: " + e.getMessage());
            return false;
        }
    }
}