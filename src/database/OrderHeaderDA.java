package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.OrderHeader;

public class OrderHeaderDA {
	private Connect connectionManager = Connect.getInstance();

	public String generateOrderId() {
		return "ORD-" + System.currentTimeMillis() % 100000;
	}

	// === SAVE ORDER (dipakai waktu Checkout) === 
	public String saveOrderHeader(String idCustomer, String idPromo, double totalAmount) {
		String newId = generateOrderId();

		String query = "INSERT INTO order_headers (id_order, id_customer, id_promo, total_amount, status, ordered_at) VALUES (?, ?, ?, ?, 'Pending', NOW())";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, newId);
			ps.setString(2, idCustomer);

			if (idPromo == null) {
				ps.setNull(3, Types.VARCHAR);
			} else {
				ps.setString(3, idPromo);
			}

			ps.setDouble(4, totalAmount);

			int result = ps.executeUpdate();
			if (result > 0)
				return newId;

		} catch (SQLException e) {
			System.out.println("Error saveOrderHeader: " + e.getMessage());
			return null;
		}
		return null;
	}

	// === AMbil History Order Customer ===
	public List<OrderHeader> getOrdersByCustomer(String idCustomer) {
		List<OrderHeader> historyList = new ArrayList<>();
		String query = "SELECT * FROM order_headers WHERE id_customer = ? ORDER BY ordered_at DESC";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, idCustomer);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OrderHeader order = new OrderHeader();
				order.setIdOrder(rs.getString("id_order"));
				order.setIdCustomer(rs.getString("id_customer"));
				order.setPromoCode(rs.getString("id_promo"));
				order.setTotalAmount(rs.getDouble("total_amount"));
				order.setTransactionDate(rs.getString("ordered_at"));
				order.setStatus(rs.getString("status"));
				historyList.add(order);
			}

		} catch (SQLException e) {
			System.out.println("Error getOrdersByCustomer: " + e.getMessage());
		}
		return historyList;
	}

	// === Ambil Semua Order untuk Admin ===
	public List<OrderHeader> getAllOrders() {
		List<OrderHeader> orderList = new ArrayList<>();

		String query = "SELECT o.id_order, o.id_customer, u.full_name, o.id_promo, o.total_amount, o.ordered_at, o.status "
				+ "FROM order_headers o " + "JOIN users u ON o.id_customer = u.id_user " + "ORDER BY o.ordered_at DESC";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query); 
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				OrderHeader order = new OrderHeader();
				order.setIdOrder(rs.getString("id_order"));
				order.setIdCustomer(rs.getString("id_customer"));
				order.setCustomerName(rs.getString("full_name")); 
				order.setPromoCode(rs.getString("id_promo"));
				order.setTotalAmount(rs.getDouble("total_amount"));
				order.setTransactionDate(rs.getString("ordered_at"));
				order.setStatus(rs.getString("status"));

				orderList.add(order);
			}

		} catch (SQLException e) {
			System.out.println("Error getAllOrders: " + e.getMessage());
		}
		return orderList;
	}

	// === Update Status (Untuk Kurir/Admin) ===
	public boolean updateStatus(String idOrder, String newStatus) {
		String query = "UPDATE order_headers SET status = ? WHERE id_order = ?";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, newStatus);
			ps.setString(2, idOrder);

			int result = ps.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			System.out.println("Error updateStatus: " + e.getMessage());
			return false;
		}
	}

	// === Get Orders (Untuk Kurir) ===
	public List<OrderHeader> getOrdersByCourier(String idCourier) {
		List<OrderHeader> taskList = new ArrayList<>();

		String query = "SELECT o.id_order, o.id_customer, u.full_name, o.id_promo, o.total_amount, o.ordered_at, o.status "
				+ "FROM order_headers o " + "JOIN deliveries d ON o.id_order = d.id_order "
				+ "JOIN users u ON o.id_customer = u.id_user " + "WHERE d.id_courier = ? "
				+ "ORDER BY o.ordered_at DESC";

		Connection conn = connectionManager.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, idCourier);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					OrderHeader order = new OrderHeader();
					order.setIdOrder(rs.getString("id_order"));
					order.setIdCustomer(rs.getString("id_customer"));
					order.setCustomerName(rs.getString("full_name"));
					order.setPromoCode(rs.getString("id_promo"));
					order.setTotalAmount(rs.getDouble("total_amount"));
					order.setTransactionDate(rs.getString("ordered_at"));
					order.setStatus(rs.getString("status"));

					taskList.add(order);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error getOrdersByCourier: " + e.getMessage());
		}
		return taskList;
	}
}