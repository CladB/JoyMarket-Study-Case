package view;

import controller.CourierHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Courier;
import model.User;

import java.util.List;

public class CourierWindow {

	private Stage stage;
	private User currentUser;
	private CourierHandler courierHandler = new CourierHandler();

	public CourierWindow(Stage stage, User currentUser) {
		this.stage = stage;
		this.currentUser = currentUser;
	}

	public Scene createCourierListScene() {
		Label lblTitle = new Label("Courier Management");
		lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		// === Table Setup ===
		TableView<Courier> table = new TableView<>();

		// === Courier ID===
		TableColumn<Courier, String> colId = new TableColumn<>("Courier ID");
		colId.setCellValueFactory(new PropertyValueFactory<>("idCourier"));
		colId.setMinWidth(80);

		// === User ID ===
		TableColumn<Courier, String> colUserId = new TableColumn<>("User ID");
		colUserId.setCellValueFactory(new PropertyValueFactory<>("idUser"));
		colUserId.setMinWidth(80);

		// === Name ===
		TableColumn<Courier, String> colName = new TableColumn<>("Name");
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colName.setMinWidth(120);

		// === Email ===
		TableColumn<Courier, String> colEmail = new TableColumn<>("Email");
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colEmail.setMinWidth(150);

		// === Phone ===
		TableColumn<Courier, String> colPhone = new TableColumn<>("Phone");
		colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		colPhone.setMinWidth(100);

		// === Address ===
		TableColumn<Courier, String> colAddress = new TableColumn<>("Address");
		colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		colAddress.setMinWidth(150);

		// === Vehicle ===
		TableColumn<Courier, String> colType = new TableColumn<>("Vehicle");
		colType.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
		colType.setMinWidth(80);

		// === Plate Number ===
		TableColumn<Courier, String> colPlate = new TableColumn<>("Plate Number");
		colPlate.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));
		colPlate.setMinWidth(100);

		// === Masukkan SEMUA kolom ke tabel ===
		table.getColumns().addAll(colId, colUserId, colName, colEmail, colPhone, colAddress, colType, colPlate);

		refreshTable(table);

		// === Back Button ===
		Button btnBack = new Button("Back to Dashboard");
		btnBack.setOnAction(e -> {
			ProductWindow productWindow = new ProductWindow(stage, currentUser.getIdUser());
			stage.setScene(productWindow.createProductScene());
		});

		// Layout
		VBox root = new VBox(15);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(lblTitle, table, btnBack);

		return new Scene(root, 1000, 600);
	}

	private void refreshTable(TableView<Courier> table) {
		List<Courier> data = courierHandler.getAllCouriers();
		ObservableList<Courier> obs = FXCollections.observableArrayList(data);
		table.setItems(obs);
	}
}