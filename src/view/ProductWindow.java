package view;

import controller.CartItemHandler;
import controller.ProductHandler;
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

import java.util.List;

public class ProductWindow {

    private ProductHandler productHandler;
    private CartItemHandler cartItemHandler;
    private Stage stage;
    private String userId; // ID Customer yang login

    public ProductWindow(Stage stage, String userId) {
        this.stage = stage;
        this.userId = userId;
        this.productHandler = new ProductHandler();
        this.cartItemHandler = new CartItemHandler();
    }

    public Scene createProductScene() {
        // 1. Judul
        Label lblTitle = new Label("Product List");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // 2. Setup Tabel
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

        // Load Data Produk
        refreshTable(table);

        // 3. Section Add to Cart (Spinner & Button)
        Label lblQty = new Label("Qty:");
        Spinner<Integer> spinnerQty = new Spinner<>(1, 100, 1);
        spinnerQty.setEditable(true);
        spinnerQty.setPrefWidth(70);

        Button btnAddToCart = new Button("Add to Cart");
        btnAddToCart.setOnAction(e -> {
            Product selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a product.");
                return;
            }
            
            int qty = spinnerQty.getValue();
            // Panggil Controller untuk insert ke database
            String result = cartItemHandler.createCartItem(userId, selected.getIdProduct(), qty);
            
            if (result.equals("Success")) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Added to Cart!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", result);
            }
        });

        // 4. Tombol Navigasi (View Cart & Logout)
        
        // === TOMBOL VIEW CART ===
        Button btnViewCart = new Button("View Cart");
        btnViewCart.setOnAction(e -> {
            // Pindah ke CartItemWindow
            CartItemWindow cartWindow = new CartItemWindow(stage, userId);
            stage.setScene(cartWindow.createCartScene());
        });

        // === TOMBOL LOGOUT ===
        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ffcccc;"); // Merah muda
        btnLogout.setOnAction(e -> {
            UserWindow userWindow = new UserWindow(stage);
            stage.setScene(userWindow.createLoginScene());
        });

        // 5. Layout Setup
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.getChildren().addAll(lblQty, spinnerQty, btnAddToCart);

        HBox navBox = new HBox(10);
        navBox.setAlignment(Pos.CENTER);
        navBox.getChildren().addAll(btnViewCart, btnLogout);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(lblTitle, table, actionBox, navBox);

        return new Scene(root, 600, 550);
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
        alert.showAndWait();
    }
}