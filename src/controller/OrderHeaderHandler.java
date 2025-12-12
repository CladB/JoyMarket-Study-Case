package controller;

import java.util.List;
import model.CartItem;
import model.Product;
import model.Promo;
import database.CustomerDA;
import database.ProductDA;
import database.PromoDA;
import database.OrderHeaderDA;
import database.OrderDetailDA;

public class OrderHeaderHandler {

	private CustomerDA customerDA = new CustomerDA();
	private ProductDA productDA = new ProductDA();
	private PromoDA promoDA = new PromoDA();
	private OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
	private OrderDetailDA orderDetailDA = new OrderDetailDA();

	/**
	 * Method Utama: Checkout Parameter idCustomer wajib ada untuk mengidentifikasi
	 * siapa yang beli.
	 */
	public String placeOrder(String idCustomer, String promoCode, List<CartItem> cartItems) {

		// 1. Validasi Cart
		if (cartItems == null || cartItems.isEmpty()) {
			return "Error: Keranjang belanja kosong.";
		}

		// Validasi Tambahan: Pastikan item di keranjang milik customer yang sedang
		// login
		// (Mencegah error data silang)
		if (!cartItems.get(0).getIdCustomer().equals(idCustomer)) {
			return "Error: Data keranjang tidak sesuai dengan user yang login.";
		}

		double finalTotalAmount = 0.0;

		// 2. Loop Cart (Validasi Stok & Hitung Total)
		for (CartItem item : cartItems) {
			Product product = productDA.getProduct(item.getIdProduct());

			if (product == null) {
				return "Error: Produk " + item.getProductName() + " tidak ditemukan.";
			}

			if (product.getStock() < item.getQuantity()) {
				return "Error: Stok " + product.getName() + " tidak mencukupi.";
			}

			// Hitung total pakai harga database
			finalTotalAmount += (product.getPrice() * item.getQuantity());
		}

		// 3. Cek Promo
		if (promoCode != null && !promoCode.isEmpty()) {
			Promo promo = promoDA.getPromo(promoCode);
			if (promo != null) {
				// Contoh diskon persen
				double discount = finalTotalAmount * (promo.getDiscountPercentage() / 100);
				finalTotalAmount -= discount;
			} else {
				return "Error: Kode Promo tidak valid.";
			}
		}

		// 4. Cek Saldo Customer (PENTING: Pakai idCustomer)
		double currentBalance = customerDA.getCurrentBalance(idCustomer);

		if (currentBalance < finalTotalAmount) {
			return "Error: Saldo tidak mencukupi.";
		}

		// 5. Simpan Transaksi
		try {
			// A. Kurangi Saldo (Pakai idCustomer)
			customerDA.updateBalance(idCustomer, currentBalance - finalTotalAmount);

			// B. Simpan Header (Pakai idCustomer)
			String newOrderId = orderHeaderDA.saveOrderHeader(idCustomer, promoCode, finalTotalAmount);

			// C. Simpan Detail
			for (CartItem item : cartItems) {
				orderDetailDA.saveDetail(newOrderId, item.getIdProduct(), item.getQuantity());
				productDA.decreaseStock(item.getIdProduct(), item.getQuantity());
			}

			return "Success: Transaksi Berhasil!";

		} catch (Exception e) {
			e.printStackTrace();
			return "Error: Gagal memproses transaksi.";
		}
	}
}