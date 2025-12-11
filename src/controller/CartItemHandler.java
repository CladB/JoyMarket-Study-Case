package controller;

import java.util.List;
import database.CartItemDA;
import model.CartItem;

public class CartItemHandler {

	CartItemDA cartItemDA = new CartItemDA();

	// Method Create (Diagram 3)
	public String createCartItem(String idCustomer, String idProduct, int count) {
		if (count < 1)
			return "Error: Jumlah minimal 1";
		return cartItemDA.saveDA(idCustomer, idProduct, count) ? "Success" : "Error";
	}

	// === Method Edit (Sesuai Diagram 4: 1.1: editCartItem) ===
	public String editCartItem(String idCustomer, String idProduct, int count) {
		if (count < 1)
			return "Error: Quantity tidak valid.";
	
		// 2. Simpan Perubahan (Memanggil DA)
		boolean isUpdated = cartItemDA.updateCartItemAmount(idCustomer, idProduct, count);

		if (isUpdated) {
			return "Success";
		} else {
			return "Error: Gagal update database.";
		}
	}

	// Helper untuk View mengambil data
	public List<CartItem> getUserCart(String idCustomer) {
		return cartItemDA.getCartItems(idCustomer);
	}

	// === BARU: Method Delete sesuai Sequence Diagram 5 (1.1: deleteCartItem) ===
	public String deleteCartItem(String idCustomer, String idProduct) {
        // Panggil DA untuk menghapus
        boolean isDeleted = cartItemDA.deleteCartItem(idCustomer, idProduct);
        
        if (isDeleted) {
            return "Success";
        } else {
            return "Error: Gagal menghapus item.";
        }
    }
}