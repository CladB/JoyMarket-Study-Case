package view;

import controller.CustomerHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerWindow {
    
    private CustomerHandler customerHandler;
    private Stage stage;
    private String userId;

    public CustomerWindow(Stage stage, String userId) {
        this.stage = stage;
        this.userId = userId;
        this.customerHandler = new CustomerHandler();
    }

    // === Method membuat Scene Top Up ===
    public Scene createTopUpScene() {
        Label lblTitle = new Label("Top Up Balance");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Tampilkan Saldo Saat Ini
        double currentBalance = customerHandler.getBalance(userId);
        Label lblCurrentBalance = new Label(String.format("Current Balance: Rp %,.2f", currentBalance));
        lblCurrentBalance.setStyle("-fx-font-size: 14px;");

        // Form Input (Activity: Display the top up balance form)
        TextField tfAmount = new TextField();
        tfAmount.setPromptText("Enter Amount (e.g., 50000)");

        Button btnTopUp = new Button("Top Up Now");
        Button btnBack = new Button("Back to Products");

        // Action Top Up (Activity: Press "Top Up" button)
        btnTopUp.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(tfAmount.getText());
                
                // Panggil Handler (Sequence: 1.1: topUpBalance)
                String result = customerHandler.topUpBalance(userId, amount);
                
                // Cek Hasil (Activity: Is Valid? -> Show Message)
                if (result.equals("Success")) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Top-Up Successful", "Saldo berhasil ditambahkan!");
                    
                    // Refresh halaman (update label saldo)
                    stage.setScene(createTopUpScene());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed", "Invalid Amount", result);
                }
                
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Masukkan angka yang valid.");
            }
        });

        // Action Back
        btnBack.setOnAction(e -> {
            ProductWindow productWindow = new ProductWindow(stage, userId);
            stage.setScene(productWindow.createProductScene());
        });

        // Layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(lblTitle, lblCurrentBalance, new Label("Top Up Amount:"), tfAmount, btnTopUp, btnBack);

        return new Scene(root, 400, 400);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}