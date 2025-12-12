package controller;

import java.util.List;
import database.CartItemDA;
import model.CartItem;

public class CartItemHandler {

	CartItemDA cartItemDA = new CartItemDA();

	public String createCartItem(String idCustomer, String idProduct, int count) {
		if (count < 1)
			return "Error: Jumlah minimal 1";
		return cartItemDA.saveDA(idCustomer, idProduct, count) ? "Success" : "Error";
	}

	// === EDIT CART ITEM ===
	public String editCartItem(String idCustomer, String idProduct, int count) {
		if (count < 1)
			return "Error: Quantity tidak valid.";

		boolean isUpdated = cartItemDA.updateCartItemAmount(idCustomer, idProduct, count);

		if (isUpdated) {
			return "Success";
		} else {
			return "Error: Gagal update database.";
		}
	}

	// View mengambil data
	public List<CartItem> getUserCart(String idCustomer) {
		return cartItemDA.getCartItems(idCustomer);
	}

	// === DELETE CART ITEM ===
	public String deleteCartItem(String idCustomer, String idProduct) {
		boolean isDeleted = cartItemDA.deleteCartItem(idCustomer, idProduct);

		if (isDeleted) {
			return "Success";
		} else {
			return "Error: Gagal menghapus item.";
		}
	}
}