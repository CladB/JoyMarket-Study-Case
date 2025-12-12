package view;

import controller.OrderHeaderHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.OrderHeader;
import model.User;

import java.util.List;

public class OrderListWindow {
	// === Untuk admin saja ===
	private Stage stage;
	private User currentUser;
	private OrderHeaderHandler orderHandler = new OrderHeaderHandler();

	public OrderListWindow(Stage stage, User currentUser) {
		this.stage = stage;
		this.currentUser = currentUser;
	}

	public Scene createOrderListScene() {
		Label lblTitle = new Label("All Incoming Orders");
		lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		// === Table Setup ===
		TableView<OrderHeader> table = new TableView<>();

		TableColumn<OrderHeader, String> colId = new TableColumn<>("Order ID");
		colId.setCellValueFactory(new PropertyValueFactory<>("idOrder"));

		TableColumn<OrderHeader, String> colCust = new TableColumn<>("Customer Name");
		colCust.setCellValueFactory(new PropertyValueFactory<>("customerName")); // Join
		colCust.setMinWidth(120);

		TableColumn<OrderHeader, String> colDate = new TableColumn<>("Date");
		colDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
		colDate.setMinWidth(150);

		TableColumn<OrderHeader, Double> colTotal = new TableColumn<>("Total");
		colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

		TableColumn<OrderHeader, String> colStatus = new TableColumn<>("Status");
		colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

		table.getColumns().addAll(colId, colCust, colDate, colTotal, colStatus);

		// === Load DAta ===
		List<OrderHeader> data = orderHandler.getAllOrdersForAdmin();
		if (data != null && !data.isEmpty()) {
			table.getItems().setAll(data);
		} else {
			table.setPlaceholder(new Label("No Order Available")); // Sesuai Activity Diagram
		}

		// === Back Button ===
		Button btnBack = new Button("Back to Dashboard");
		btnBack.setOnAction(e -> {
			ProductWindow productWindow = new ProductWindow(stage, currentUser.getIdUser());
			stage.setScene(productWindow.createProductScene());
		});
		
		// === Assign Courier Button ===
		Button btnAssign = new Button("Assign Courier");
        btnAssign.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnAssign.setOnAction(e -> {
        	
            OrderHeader selectedOrder = table.getSelectionModel().getSelectedItem(); // Ambil order yang dipilih di tabel
            
            if (selectedOrder != null) {
                // === Show Popup DeliveryWindow ===
                DeliveryWindow deliveryWin = new DeliveryWindow();
                deliveryWin.showAssignCourierWindow(selectedOrder);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("Pilih order yang ingin ditugaskan kurirnya.");
                alert.show();
            }
        });

		// === Layout ===
		VBox root = new VBox(15);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(lblTitle, btnAssign, table, btnBack);

		return new Scene(root, 800, 600);
	}
}