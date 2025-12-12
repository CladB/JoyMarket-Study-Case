package view;

import controller.OrderHeaderHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.OrderHeader;

import java.util.List;

public class OrderHistoryWindow {

	private Stage stage;
	private String idCustomer;
	private OrderHeaderHandler orderHandler = new OrderHeaderHandler();

	public OrderHistoryWindow(Stage stage, String idCustomer) {
		this.stage = stage;
		this.idCustomer = idCustomer;
	}

	public Scene createHistoryScene() {
		// === Title ===
		Label lblTitle = new Label("My Order History");
		lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		// === Table Setup ===
		TableView<OrderHeader> table = new TableView<>();

		TableColumn<OrderHeader, String> colId = new TableColumn<>("Order ID");
		colId.setCellValueFactory(new PropertyValueFactory<>("idOrder"));

		TableColumn<OrderHeader, String> colDate = new TableColumn<>("Date");
		colDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
		colDate.setMinWidth(150);

		TableColumn<OrderHeader, Double> colTotal = new TableColumn<>("Total Amount");
		colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
		colTotal.setMinWidth(120);

		TableColumn<OrderHeader, String> colPromo = new TableColumn<>("Promo");
		colPromo.setCellValueFactory(new PropertyValueFactory<>("promoCode"));

		table.getColumns().addAll(colId, colDate, colTotal, colPromo);

		// === Load Data ===
		List<OrderHeader> history = orderHandler.getOrderHistory(idCustomer);

		if (history != null && !history.isEmpty()) {
			table.getItems().setAll(history);
		} else {
			// === Jika kosong show now order history ===
			table.setPlaceholder(new Label("No Order History Available"));
		}

		// === Back Button ===
		Button btnBack = new Button("Back to Products");
		btnBack.setOnAction(e -> {
			ProductWindow productWindow = new ProductWindow(stage, idCustomer);
			stage.setScene(productWindow.createProductScene());
		});

		// === Layout ===
		VBox root = new VBox(15);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(lblTitle, table, btnBack);

		return new Scene(root, 600, 500);
	}
}