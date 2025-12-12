package view;

import controller.CartItemHandler;
import controller.ProductHandler;
import controller.UserHandler;
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
import model.Product;
import model.User;

import java.util.List;

public class ProductWindow {

	private ProductHandler productHandler;
	private CartItemHandler cartItemHandler;
	private UserHandler userHandler = new UserHandler();

	private Stage stage;
	private User currentUser;
	private String userId;

	public ProductWindow(Stage stage, String userId) {
		this.stage = stage;
		this.userId = userId;
		this.productHandler = new ProductHandler();
		this.cartItemHandler = new CartItemHandler();
		this.currentUser = userHandler.getUser(userId);
	}

	public Scene createProductScene() {
		String role = (currentUser != null) ? currentUser.getRole() : "Customer";

		Label lblTitle = new Label("Product List - " + role);
		lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		TableView<Product> table = new TableView<>();

		TableColumn<Product, String> colName = new TableColumn<>("Name");
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colName.setMinWidth(150);

		TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

		TableColumn<Product, Integer> colStock = new TableColumn<>("Stock");
		colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

		TableColumn<Product, String> colCat = new TableColumn<>("Category");
		colCat.setCellValueFactory(new PropertyValueFactory<>("category"));

		table.getColumns().addAll(colName, colPrice, colStock, colCat);
		refreshTable(table);
		
		 // === TOMBOL EDIT PROFILE ===
        Button btnEditProfile = new Button("Edit Profile");
        btnEditProfile.setOnAction(e -> {
            ProfileWindow profileWin = new ProfileWindow(stage, currentUser);
            stage.setScene(profileWin.createEditProfileScene());
        });

		// === 1. SECTION CUSTOMER (Add to Cart) ===
		Label lblQty = new Label("Qty:");
		Spinner<Integer> spinnerQty = new Spinner<>(1, 100, 1);
		spinnerQty.setEditable(true);
		Button btnAddToCart = new Button("Add to Cart");

		btnAddToCart.setOnAction(e -> {
			Product selected = table.getSelectionModel().getSelectedItem();
			if (selected == null) {
				showAlert(Alert.AlertType.WARNING, "Warning", "Pilih produk dulu.");
				return;
			}
			cartItemHandler.createCartItem(userId, selected.getIdProduct(), spinnerQty.getValue());
			refreshTable(table);
			showAlert(Alert.AlertType.INFORMATION, "Success", "Added to Cart!");
		});

		HBox customerBox = new HBox(10, lblQty, spinnerQty, btnAddToCart);
		customerBox.setAlignment(Pos.CENTER);

		// === 2. SECTION ADMIN (Update Stock - Replace) ===
		// === View All Orders ===
        Button btnAllOrders = new Button("View All Orders");
        btnAllOrders.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white;");

        btnAllOrders.setOnAction(e -> {
            OrderListWindow orderListWin = new OrderListWindow(stage, currentUser);
            stage.setScene(orderListWin.createOrderListScene());
        });
		
        // Add Stock untuk Admin
		Label lblAdmin = new Label("Admin Stock:");
		TextField tfNewStock = new TextField();
		tfNewStock.setPromptText("New Stock"); 
		Button btnUpdateStock = new Button("Update"); 

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				tfNewStock.setText(String.valueOf(newVal.getStock()));
			}
		});

		btnUpdateStock.setOnAction(e -> {
			Product selected = table.getSelectionModel().getSelectedItem();
			if (selected != null) {
				try {
					int newStock = Integer.parseInt(tfNewStock.getText());
					// Panggil Handler
					String result = productHandler.editProductStock(selected.getIdProduct(), newStock);

					if (result.startsWith("Success")) {
						showAlert(Alert.AlertType.INFORMATION, "Success", result);
						refreshTable(table);
					} else {
						showAlert(Alert.AlertType.ERROR, "Error", result);
					}
				} catch (Exception ex) {
					showAlert(Alert.AlertType.ERROR, "Error", "Input stok harus angka.");
				}
			} else {
				showAlert(Alert.AlertType.WARNING, "Warning", "Pilih produk untuk diedit.");
			}
		});

		HBox adminBox = new HBox(10, lblAdmin, tfNewStock, btnUpdateStock);
		adminBox.setAlignment(Pos.CENTER);
		adminBox.setStyle("-fx-border-color: red; -fx-padding: 10px;");
		
		// All User
		HBox navBox = new HBox(10);
        navBox.setAlignment(Pos.CENTER);

		// === 3. LOGIC VISIBILITY ===
		if ("Admin".equals(role)) {
			customerBox.setVisible(false);
			customerBox.setManaged(false);
			adminBox.setVisible(true);
			adminBox.setManaged(true);
		} else {
			customerBox.setVisible(true);
			customerBox.setManaged(true);
			adminBox.setVisible(false);
			adminBox.setManaged(false);
		}

		// === 4. NAVIGASI ===
		Button btnViewCart = new Button("View Cart");
		Button btnHistory = new Button("Order History");
		Button btnTopUp = new Button("Top Up Balance");
		Button btnLogout = new Button("Logout");
		Button btnManageCourier = new Button("Manage Couriers");
		btnManageCourier.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white;");

		btnViewCart.setOnAction(e -> {
			CartItemWindow cartWindow = new CartItemWindow(stage, userId);
			stage.setScene(cartWindow.createCartScene());
		});

		btnHistory.setOnAction(e -> {
			OrderHistoryWindow historyWindow = new OrderHistoryWindow(stage, userId);
			stage.setScene(historyWindow.createHistoryScene());
		});

		btnTopUp.setOnAction(e -> {
			CustomerWindow custWindow = new CustomerWindow(stage, userId);
			stage.setScene(custWindow.createTopUpScene());
		});

		btnLogout.setOnAction(e -> {
			UserWindow userWindow = new UserWindow(stage);
			stage.setScene(userWindow.createLoginScene());
		});

		btnManageCourier.setOnAction(e -> {
			CourierWindow courierWin = new CourierWindow(stage, currentUser);
			stage.setScene(courierWin.createCourierListScene());
		});

		if ("Admin".equals(role)) {
            // Admin: Manage Couriers, View Orders, Logout
            navBox.getChildren().addAll(btnManageCourier, btnAllOrders, btnLogout);
        } else {
            // Customer
            navBox.getChildren().addAll(btnViewCart, btnEditProfile, btnHistory, btnTopUp, btnLogout);
        }

		VBox root = new VBox(15);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(lblTitle, table, customerBox, adminBox, navBox);

		return new Scene(root, 750, 650);
	}

	private void refreshTable(TableView<Product> table) {
		List<Product> data = productHandler.getAllProducts();
		ObservableList<Product> obs = FXCollections.observableArrayList(data);
		table.setItems(obs);
	}

	private void showAlert(Alert.AlertType type, String title, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.show();
	}
}