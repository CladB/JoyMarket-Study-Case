package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import model.CartItem;
import controller.OrderHeaderHandler;
import controller.CartItemHandler;

public class OrderHeaderWindow extends Application {

	private String idCustomer;
	private Stage stage;

	// --- Controller ---
	// Controller untuk checkout
	private OrderHeaderHandler orderHandler = new OrderHeaderHandler();
	// Controller untuk mengambil data keranjang (Asumsi ada sesuai Class Diagram)
	private CartItemHandler cartHandler = new CartItemHandler();

	// --- UI Components ---
	private TableView<CartItem> tableCart;
	private TextField txtPromoCode;
	private Label lblTotalAmount;
	private Label lblMessage; // Untuk menampilkan pesan Success/Error

	// --- Data ---
	private List<CartItem> currentCartList;

	// Constructor menerima ID Customer yang sedang login
	public OrderHeaderWindow(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		stage.setTitle("Checkout & Place Order - JoyMarket");

		// 1. Setup Tabel Keranjang
		setupTable();

		// 2. Load Data dari Database (via CartItemHandler)
		loadCartData();

		// 3. Area Kode Promo
		Label lblPromo = new Label("Kode Promo:");
		txtPromoCode = new TextField();
		txtPromoCode.setPromptText("Masukkan kode promo...");

		HBox promoBox = new HBox(10, lblPromo, txtPromoCode);
		promoBox.setAlignment(Pos.CENTER_LEFT);

		// 4. Area Total & Tombol Action
		lblTotalAmount = new Label("Total: Rp 0");
		lblTotalAmount.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

		Button btnCheckout = new Button("Checkout / Place Order");
		btnCheckout.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
		btnCheckout.setMinWidth(150);

		// EVENT: Tombol Checkout ditekan
		btnCheckout.setOnAction(e -> handleCheckout());

		lblMessage = new Label();
		lblMessage.setStyle("-fx-text-fill: red;");

		// 5. Layout Utama (VBox)
		VBox mainLayout = new VBox(15);
		mainLayout.setPadding(new Insets(20));
		mainLayout.getChildren().addAll(new Label("Daftar Belanjaan Anda:"), tableCart, promoBox, new Separator(),
				lblTotalAmount, btnCheckout, lblMessage);

		Scene scene = new Scene(mainLayout, 600, 550);
		stage.setScene(scene);
		stage.show();
	}

	// --- Helper: Setup Kolom Tabel ---
	private void setupTable() {
		tableCart = new TableView<>();

		// Kolom Produk (Sesuai getter di model CartItem: getProductName)
		TableColumn<CartItem, String> colProduct = new TableColumn<>("Nama Produk");
		colProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
		colProduct.setMinWidth(200);

		// Kolom Harga
		TableColumn<CartItem, Double> colPrice = new TableColumn<>("Harga");
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

		// Kolom Qty
		TableColumn<CartItem, Integer> colQty = new TableColumn<>("Qty");
		colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		// Kolom Total (Subtotal)
		TableColumn<CartItem, Double> colTotal = new TableColumn<>("Subtotal");
		colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

		tableCart.getColumns().addAll(colProduct, colPrice, colQty, colTotal);
	}

	// --- Helper: Load Data ---
	private void loadCartData() {
		// Activity Diagram: "Fetch cart item list"
		// Memanggil CartItemHandler untuk mengambil list barang milik user ini

		// PENTING: Pastikan Anda sudah membuat class CartItemHandler.
		// Jika belum, method ini akan error.
		currentCartList = cartHandler.getUserCart(idCustomer); 

	    if (currentCartList != null && !currentCartList.isEmpty()) {
	        tableCart.getItems().setAll(currentCartList);
	        refreshTotalLabel();
	    } else {
	        lblMessage.setText("Keranjang Anda kosong.");
	    }
	}

	private void refreshTotalLabel() {
		double total = 0;
		if (currentCartList != null) {
			for (CartItem item : currentCartList) {
				total += item.getTotal();
			}
		}
		lblTotalAmount.setText("Total Estimasi: Rp " + String.format("%.2f", total));
	}

	// --- Logic Utama: Handle Checkout ---
	private void handleCheckout() {
		String promoCode = txtPromoCode.getText();

		// Panggil Controller (Sequence Diagram Step 2: createOrderHeader -> placeOrder)
		// Kita kirim List<CartItem> yang ada di tampilan ke controller
		String result = orderHandler.placeOrder(idCustomer, promoCode, currentCartList);

		// Tampilkan Hasil (Activity: Show Message)
		if (result.startsWith("Success")) {
			lblMessage.setStyle("-fx-text-fill: green;");
			lblMessage.setText(result);

			// Tampilkan Alert Sukses
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText(null);
			alert.setContentText(result);
			alert.showAndWait();

			// Clear Cart Tampilan & Tutup Window (Opsional)
			tableCart.getItems().clear();
			stage.close();

		} else {
			lblMessage.setStyle("-fx-text-fill: red;");
			lblMessage.setText(result);

			// Tampilkan Alert Error
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Checkout Gagal");
			alert.setHeaderText(null);
			alert.setContentText(result);
			alert.show();
		}
	}

}