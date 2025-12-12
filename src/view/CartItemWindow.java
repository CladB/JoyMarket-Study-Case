package view;

import controller.CartItemHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CartItem;

import java.util.List;
import java.util.Optional;

public class CartItemWindow {

	private CartItemHandler cartItemHandler;
	private Stage stage;
	private String userId;

	public CartItemWindow(Stage stage, String userId) {
		this.stage = stage;
		this.userId = userId;
		this.cartItemHandler = new CartItemHandler();
	}

	public Scene createCartScene() {
		Label lblTitle = new Label("Your Cart");
		lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		// === Table Setup ===
		TableView<CartItem> table = new TableView<>();
		
		TableColumn<CartItem, String> colName = new TableColumn<>("Product");
		colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
		colName.setMinWidth(150);

		TableColumn<CartItem, Double> colPrice = new TableColumn<>("Price");
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

		TableColumn<CartItem, Integer> colQty = new TableColumn<>("Qty");
		colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<CartItem, Double> colTotal = new TableColumn<>("Total");
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

		table.getColumns().addAll(colName, colPrice, colQty, colTotal);
		refreshTable(table);

		// === Komponen Edit & Update ===
		Label lblEdit = new Label("Edit Qty:");
		Spinner<Integer> spinnerQty = new Spinner<>(1, 100, 1);
		spinnerQty.setEditable(true);

		// === Listener tabel ===
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null)
				spinnerQty.getValueFactory().setValue(newVal.getQuantity());
		});

		// === Update Button ===
		Button btnUpdate = new Button("Update");
		btnUpdate.setOnAction(e -> {
			CartItem selected = table.getSelectionModel().getSelectedItem();
			if (selected == null)
				return;
			cartItemHandler.editCartItem(userId, selected.getIdProduct(), spinnerQty.getValue());
			refreshTable(table);
		});

		// === Delete Button ===
		Button btnDelete = new Button("Delete");
		btnDelete.setStyle("-fx-background-color: #ffcccc; -fx-text-fill: red;");
		btnDelete.setOnAction(e -> {
			CartItem selected = table.getSelectionModel().getSelectedItem();
			if (selected != null) {
				cartItemHandler.deleteCartItem(userId, selected.getIdProduct());
				refreshTable(table);
			}
		});
		
		// === Checkout Button === 
		Button btnCheckout = new Button("Checkout");
		btnCheckout.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

		btnCheckout.setOnAction(e -> {
			if (table.getItems().isEmpty()) {
				showAlert(Alert.AlertType.WARNING, "Cart Empty", "Belanja dulu bos!");
				return;
			}

			try {
				OrderHeaderWindow orderWindow = new OrderHeaderWindow(stage, userId);
				stage.setScene(orderWindow.createOrderScene());

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		// === Back Button ===
		Button btnBack = new Button("Back to Products");
		btnBack.setOnAction(e -> {
			ProductWindow prodWindow = new ProductWindow(stage, userId);
			stage.setScene(prodWindow.createProductScene());
		});

		// Layout Action
		HBox actionBox = new HBox(10);
		actionBox.setAlignment(Pos.CENTER);

		actionBox.getChildren().addAll(lblEdit, spinnerQty, btnUpdate, btnDelete, btnCheckout);

		VBox root = new VBox(15);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(lblTitle, table, actionBox, btnBack);

		return new Scene(root, 700, 550);
	}
	
	// === Refresh Table ===
	private void refreshTable(TableView<CartItem> table) {
		List<CartItem> data = cartItemHandler.getUserCart(userId);
		ObservableList<CartItem> obs = FXCollections.observableArrayList(data);
		table.setItems(obs);
	}

	// === Show Alert ===
	private void showAlert(Alert.AlertType type, String title, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.show();
	}
}