package view;

import controller.DeliveryHandler;
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

public class CourierTaskWindow {
	private Stage stage;
	private User currentUser; 
	private DeliveryHandler deliveryHandler = new DeliveryHandler();

	public CourierTaskWindow(Stage stage, User currentUser) {
		this.stage = stage;
		this.currentUser = currentUser;
	}

	public Scene createCourierScene() {
		Label lblTitle = new Label("My Delivery Tasks");
		lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
		Label lblWelcome = new Label("Welcome, " + currentUser.getFullName());

		// === Table Setup ===
		TableView<OrderHeader> table = new TableView<>();

		TableColumn<OrderHeader, String> colId = new TableColumn<>("Order ID");
		colId.setCellValueFactory(new PropertyValueFactory<>("idOrder"));

		TableColumn<OrderHeader, String> colCust = new TableColumn<>("Customer");
		colCust.setCellValueFactory(new PropertyValueFactory<>("customerName"));
		colCust.setMinWidth(120);

		TableColumn<OrderHeader, String> colAddress = new TableColumn<>("Date");
		colAddress.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
		colAddress.setMinWidth(150);

		TableColumn<OrderHeader, String> colStatus = new TableColumn<>("Current Status");
		colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		colStatus.setMinWidth(100);

		table.getColumns().addAll(colId, colCust, colAddress, colStatus);

		refreshTable(table);

		// === Logout Button ===
		Button btnLogout = new Button("Logout");
		btnLogout.setStyle("-fx-background-color: #ffcccc;");
		btnLogout.setOnAction(e -> {
			UserWindow userWindow = new UserWindow(stage);
			stage.setScene(userWindow.createLoginScene());
		});

		// === Update Status Button ===
		Button btnUpdateStatus = new Button("Update Status");
		btnUpdateStatus.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

		btnUpdateStatus.setOnAction(e -> {
			OrderHeader selectedOrder = table.getSelectionModel().getSelectedItem();

			if (selectedOrder != null) {
				// Buka Popup StatusUpdateWindow
				StatusUpdateWindow updateWin = new StatusUpdateWindow();
				updateWin.showUpdateStatus(selectedOrder);

				// Refresh tabel supaya setelah pop up nya diclose langsung terupdate
				refreshTable(table);
			} else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setContentText("Pilih tugas yang ingin diupdate.");
				alert.show();
			}
		});

		// Layout
		VBox root = new VBox(15);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(lblTitle, lblWelcome, table, btnUpdateStatus, btnLogout);

		return new Scene(root, 700, 500);
	}

	private void refreshTable(TableView<OrderHeader> table) {
		List<OrderHeader> tasks = deliveryHandler.getCourierTasks(currentUser.getIdUser());

		if (tasks != null && !tasks.isEmpty()) {
			ObservableList<OrderHeader> obs = FXCollections.observableArrayList(tasks);
			table.setItems(obs);
		} else {
			table.setPlaceholder(new Label("No Assigned Delivery Available"));
		}
	}
}
