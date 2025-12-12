package controller;

import database.DeliveryDA;
import database.OrderHeaderDA;
import model.Delivery;
import model.OrderHeader;
import java.util.List;

public class DeliveryHandler {

	private DeliveryDA deliveryDA = new DeliveryDA();
	private OrderHeaderDA orderHeaderDA = new OrderHeaderDA();
	
	public List<OrderHeader> getCourierTasks(String idCourier) {
		return orderHeaderDA.getOrdersByCourier(idCourier);
	}

	// === Method untuk Assign Courier ===
	public String assignCourier(String idOrder, String idCourier) {
		if (idOrder == null || idOrder.isEmpty())
			return "Error: Order tidak valid.";
		if (idCourier == null || idCourier.isEmpty())
			return "Error: Pilih kurir terlebih dahulu.";

		Delivery delivery = new Delivery(idOrder, idCourier, "Assigned");

		boolean isDeliverySaved = deliveryDA.saveDelivery(delivery);

		if (isDeliverySaved) {
			return "Success: Kurir berhasil ditugaskan!";
		} else {
			return "Error: Gagal menyimpan data.";
		}
	}

	// === Edit Delivery Status ===
	public String editDeliveryStatus(String idOrder, String newStatus) {

		// === Validasi ===
		if (idOrder == null || idOrder.isEmpty())
			return "Error: Order tidak valid.";
		if (newStatus == null || newStatus.isEmpty())
			return "Error: Status tidak valid.";

		// === Update di Tabel Deliveries ===
		boolean isDeliveryUpdated = deliveryDA.updateStatus(idOrder, newStatus);

		// === Update di Tabel Order Headers ===
		boolean isOrderUpdated = orderHeaderDA.updateStatus(idOrder, newStatus);

		if (isDeliveryUpdated && isOrderUpdated) {
			return "Success: Status berhasil diperbarui!";
		} else {
			return "Error: Gagal mengupdate status di database.";
		}
	}
}