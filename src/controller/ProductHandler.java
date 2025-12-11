package controller;

import java.util.List;
import database.ProductDA;
import model.Product;

public class ProductHandler {
    
    ProductDA productDA = new ProductDA();

    // Mengambil semua list produk untuk ditampilkan di Table
    public List<Product> getAllProducts() {
        // Memanggil method read() dari ProductDA
        return productDA.read();
    }
}