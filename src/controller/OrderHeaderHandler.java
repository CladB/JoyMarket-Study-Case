package controller;

import java.util.List;
import model.CartItem;
import model.OrderHeader;
import model.Product;
import model.Promo;
import database.CustomerDA;
import database.ProductDA;
import database.PromoDA;
import database.OrderHeaderDA;
import database.OrderDetailDA;
import database.CartItemDA; // <--- JANGAN LUPA IMPORT INI

public class OrderHeaderHandler {

	private CustomerDA customerDA = new CustomerDA();
	private ProductDA productDA = new ProductDA();
	private PromoDA promoDA = new PromoDA();
	private OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
	private OrderDetailDA orderDetailDA = new OrderDetailDA();
	private CartItemDA cartItemDA = new CartItemDA();

	public String placeOrder(String idCustomer, String promoCode, List<CartItem> cartItems) {

		// === Validasi Cart ===
		if (cartItems == null || cartItems.isEmpty())
			return "Error: Keranjang belanja kosong.";

		// === Hitung Total Awal & Cek Stok ===
		double grossTotal = 0.0;
		for (CartItem item : cartItems) {
			Product product = productDA.getProduct(item.getIdProduct());
			if (product == null)
				return "Error: Produk " + item.getIdProduct() + " tidak ditemukan.";
			if (product.getStock() < item.getQuantity())
				return "Error: Stok " + product.getName() + " habis.";
			grossTotal += (product.getPrice() * item.getQuantity());
		}

		// === LOGIC PROMO ===
		double finalTotalAmount = grossTotal;
		String validPromoId = null; 

		if (promoCode != null && !promoCode.isEmpty()) {
			Promo promo = promoDA.getPromoByCode(promoCode);

			if (promo != null) {
				// === Logic Diskon ===
				double discount = grossTotal * (promo.getDiscountPercentage() / 100);
				finalTotalAmount = grossTotal - discount;

				validPromoId = promo.getIdPromo();
			} else {
				return "Error: Kode Promo '" + promoCode + "' tidak ditemukan.";
			}
		}

		// === Cek Balance Customer ===
		double currentBalance = customerDA.getCurrentBalance(idCustomer);
		if (currentBalance < finalTotalAmount) {
			return "Error: Saldo tidak mencukupi. Total: " + finalTotalAmount;
		}

		// === PROSES TRANSAKSI ===
		try {
			// === Perhitungan Saldo ===
			customerDA.updateBalance(idCustomer, currentBalance - finalTotalAmount);

			// Save order headernya
			String newOrderId = orderHeaderDA.saveOrderHeader(idCustomer, validPromoId, finalTotalAmount);

			if (newOrderId == null) {
				throw new Exception("Gagal membuat Order ID.");
			}

			// === Simpan Detail -> Kurangi Stok -> Hapus Cart Itemnya ===
			for (CartItem item : cartItems) {
				orderDetailDA.saveDetail(newOrderId, item.getIdProduct(), item.getQuantity());
				productDA.decreaseStock(item.getIdProduct(), item.getQuantity());
				cartItemDA.deleteCartItem(idCustomer, item.getIdProduct());
			}

			return "Success: Transaksi Berhasil! Total: " + finalTotalAmount;

		} catch (Exception e) {
			// === ROLLBACK SYSTEM (antisipasi uang hilang ketika error) ===
			System.out.println("Terjadi Error: " + e.getMessage());
			System.out.println("Mengembalikan Saldo Customer...");

			// Untuk kembalikan saldo ke jumlah awal
			customerDA.updateBalance(idCustomer, currentBalance);

			e.printStackTrace();
			return "Error: Transaksi Gagal (Saldo Anda telah dikembalikan). Detail: " + e.getMessage();
		}
	}

	public List<OrderHeader> getOrderHistory(String idCustomer) {
		return orderHeaderDA.getOrdersByCustomer(idCustomer);
	}

	public List<OrderHeader> getAllOrdersForAdmin() {
		return orderHeaderDA.getAllOrders();
	}
}