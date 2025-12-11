package database;

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
                Product p = new Product(
                    rs.getString("id_product"),
                    rs.getString("name"),
                    rs.getBigDecimal("price").doubleValue(),
                    rs.getInt("stock"),
                    rs.getString("category")
                );
                productList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return productList;
    }
}