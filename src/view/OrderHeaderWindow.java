package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import model.CartItem;
import controller.OrderHeaderHandler;
import controller.CartItemHandler;

public class OrderHeaderWindow {

	private String idCustomer;
	private Stage stage;

	private OrderHeaderHandler orderHandler = new OrderHeaderHandler();
	private CartItemHandler cartHandler = new CartItemHandler();

	private TableView<CartItem> tableCart;
	private TextField txtPromoCode;
	private Label lblTotalAmount;
	private Label lblMessage;

	private List<CartItem> currentCartList;

	public OrderHeaderWindow(Stage stage, String idCustomer) {
		this.stage = stage;
		this.idCustomer = idCustomer;
	}

	// === Checkout ===
	public Scene createOrderScene() {
		stage.setTitle("Checkout & Place Order - JoyMarket");

		// === Table Setup ===
		setupTable();

		// === UI component ===
		Label lblTitle = new Label("Checkout Confirmation");
		lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		Label lblPromo = new Label("Kode Promo:");
		txtPromoCode = new TextField();
		txtPromoCode.setPromptText("Masukkan kode promo...");

		HBox promoBox = new HBox(10, lblPromo, txtPromoCode);
		promoBox.setAlignment(Pos.CENTER_LEFT);

		lblTotalAmount = new Label("Total: Rp 0");
		lblTotalAmount.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

		lblMessage = new Label();

		// === Action Button ===
		Button btnProcess = new Button("Process Payment");
		btnProcess.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
		btnProcess.setMinWidth(150);

		Button btnBack = new Button("Cancel / Back");

		// === Event Handler ===
		btnProcess.setOnAction(e -> handleCheckout());

		btnBack.setOnAction(e -> {
			CartItemWindow cartWindow = new CartItemWindow(stage, idCustomer);
			stage.setScene(cartWindow.createCartScene());
		});

		// === Layout ===
		VBox mainLayout = new VBox(15);
		mainLayout.setPadding(new Insets(20));
		mainLayout.getChildren().addAll(lblTitle, tableCart, promoBox, new Separator(), lblTotalAmount,
				new HBox(10, btnProcess, btnBack), lblMessage);
		loadCartData();

		return new Scene(mainLayout, 600, 600);
	}

	private void setupTable() {
		tableCart = new TableView<>();

		TableColumn<CartItem, String> colProduct = new TableColumn<>("Produk");
		colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
		colProduct.setMinWidth(200);

		TableColumn<CartItem, Double> colPrice = new TableColumn<>("Harga");
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

		TableColumn<CartItem, Integer> colQty = new TableColumn<>("Qty");
		colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<CartItem, Double> colTotal = new TableColumn<>("Subtotal");
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

		tableCart.getColumns().addAll(colProduct, colPrice, colQty, colTotal);
	}

	private void loadCartData() {
		currentCartList = cartHandler.getUserCart(idCustomer);
		if (currentCartList != null && !currentCartList.isEmpty()) {
			tableCart.getItems().setAll(currentCartList);
			refreshTotalLabel();
		} else {
			lblMessage.setText("Keranjang kosong.");
		}
	}

	private void refreshTotalLabel() {
		double total = 0;
		if (currentCartList != null) {
			for (CartItem item : currentCartList) {
				total += item.getTotal();
			}
		}
		lblTotalAmount.setText("Total Bayar: Rp " + String.format("%.2f", total));
	}

	// === Handle Checkout ===
	private void handleCheckout() {
		String promoCode = txtPromoCode.getText();
		String result = orderHandler.placeOrder(idCustomer, promoCode, currentCartList);

		if (result.startsWith("Success")) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText(null);
			alert.setContentText(result);
			alert.showAndWait();

			// === Sukses -> Kembali ke Halaman Produk ===
			ProductWindow productWindow = new ProductWindow(stage, idCustomer);
			stage.setScene(productWindow.createProductScene());
		} else {
			lblMessage.setStyle("-fx-text-fill: red;");
			lblMessage.setText(result);

			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Failed");
			alert.setHeaderText(null);
			alert.setContentText(result);
			alert.show();
		}
	}
}