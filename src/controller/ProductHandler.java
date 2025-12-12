package controller;

import java.util.List;
import database.ProductDA;
import model.Product;

public class ProductHandler {

	ProductDA productDA = new ProductDA();

	public List<Product> getAllProducts() {
		return productDA.read();
	}

	// === EDIT STOCK ===
	public String editProductStock(String idProduct, int newStock) {

		// === Validasi: Stok tidak boleh negatif (tapi boleh 0 atau habis) ===
		if (newStock < 0) {
			return "Error: Stok tidak boleh negatif.";
		}

		// === Cek Produk apakah ada atau tidak ===
		Product product = productDA.getProduct(idProduct);
		if (product == null) {
			return "Error: Produk tidak ditemukan.";
		}

		// === Save product stocknya ===
		boolean isSuccess = productDA.updateProductStock(idProduct, newStock);

		if (isSuccess) {
			return "Success: Stok berhasil diperbarui!";
		} else {
			return "Error: Gagal menyimpan data ke database.";
		}
	}
}