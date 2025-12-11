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

		// 1. Setup Tabel
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

		// 2. Form Edit
		Label lblEdit = new Label("Edit Qty:");
		Spinner<Integer> spinnerQty = new Spinner<>(1, 100, 1);
		spinnerQty.setEditable(true);
		Button btnUpdate = new Button("Update");

		// Listener Seleksi Tabel
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				spinnerQty.getValueFactory().setValue(newVal.getQuantity());
			}
		});

		// Action Update
		btnUpdate.setOnAction(e -> {
			CartItem selected = table.getSelectionModel().getSelectedItem();
			if (selected == null) {
				showAlert(Alert.AlertType.WARNING, "Warning", "No Selection", "Pilih item dulu.");
				return;
			}
			int newCount = spinnerQty.getValue();
			String result = cartItemHandler.editCartItem(userId, selected.getIdProduct(), newCount);
			if (result.equals("Success")) {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Updated", "Quantity Updated.");
				refreshTable(table);
			} else {
				showAlert(Alert.AlertType.ERROR, "Failed", "Error", result);
			}
		});

		// === BARU: TOMBOL DELETE (Activity: Press "Delete" button) ===
		Button btnDelete = new Button("Delete Item");
		btnDelete.setStyle("-fx-background-color: #ffcccc; -fx-text-fill: red;");

		btnDelete.setOnAction(e -> {
			// 1. Ambil Item Terpilih (Activity: Select one of the cart items)
			CartItem selected = table.getSelectionModel().getSelectedItem();

			if (selected == null) {
				showAlert(Alert.AlertType.WARNING, "Warning", "No Selection", "Please select an item to delete.");
				return;
			}

			// Konfirmasi Hapus (Opsional tapi bagus untuk UX)
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setTitle("Delete Item");
			confirm.setHeaderText("Are you sure?");
			confirm.setContentText("Delete " + selected.getProductName() + " from cart?");
			Optional<ButtonType> option = confirm.showAndWait();

			if (option.isPresent() && option.get() == ButtonType.OK) {
				// 2. Panggil Handler (Sequence: 1.1: deleteCartItem)
				String result = cartItemHandler.deleteCartItem(userId, selected.getIdProduct());

				// 3. Cek Hasil (Activity: Was deleted successfully?)
				if (result.equals("Success")) {
					showAlert(Alert.AlertType.INFORMATION, "Success", "Deleted", "Item removed from cart.");
					refreshTable(table); // Refresh Tabel
				} else {
					showAlert(Alert.AlertType.ERROR, "Failed", "Error", result);
				}
			}
		});

		// Tombol Navigasi Kembali
		Button btnBack = new Button("Back to Products");
		btnBack.setOnAction(e -> {
			ProductWindow productWindow = new ProductWindow(stage, userId);
			stage.setScene(productWindow.createProductScene());
		});

		// Layout Action (Update & Delete)
		HBox actionBox = new HBox(10);
		actionBox.setAlignment(Pos.CENTER);
		actionBox.getChildren().addAll(lblEdit, spinnerQty, btnUpdate, btnDelete);

		VBox root = new VBox(15);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(lblTitle, table, actionBox, btnBack);

		return new Scene(root, 600, 500);
	}

	private void refreshTable(TableView<CartItem> table) {
		List<CartItem> data = cartItemHandler.getUserCart(userId);
		ObservableList<CartItem> obs = FXCollections.observableArrayList(data);
		table.setItems(obs);
	}

	private void showAlert(Alert.AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}