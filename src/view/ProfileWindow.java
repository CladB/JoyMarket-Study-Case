package view;

import controller.UserHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class ProfileWindow {
    private Stage stage;
    private User currentUser;
    private UserHandler userHandler = new UserHandler();

    public ProfileWindow(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }

    public Scene createEditProfileScene() {
        Label lblTitle = new Label("Edit Profile");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // === Form Input Edit (Pre-filled dengan data currentUser) ===
        Label lblName = new Label("Full Name:");
        TextField tfName = new TextField(currentUser.getFullName());

        Label lblEmail = new Label("Email:");
        TextField tfEmail = new TextField(currentUser.getEmail());
        tfEmail.setDisable(true); 
        
        Label lblPass = new Label("Password:");
        PasswordField pfPass = new PasswordField();
        pfPass.setText(currentUser.getPassword()); 

        Label lblPhone = new Label("Phone:");
        TextField tfPhone = new TextField(currentUser.getPhone());

        Label lblAddress = new Label("Address:");
        TextArea taAddress = new TextArea(currentUser.getAddress());
        taAddress.setPrefHeight(80);

        Button btnUpdate = new Button("Update Profile");
        Button btnBack = new Button("Back to Dashboard");

        // === Button Function ===
        btnUpdate.setOnAction(e -> {
            // Panggil Handler
            String result = userHandler.editProfile(
                currentUser.getIdUser(),
                tfName.getText(),
                tfEmail.getText(), 
                pfPass.getText(),
                tfPhone.getText(),
                taAddress.getText()
            );

            if (result.equals("Success")) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile Updated!");
                currentUser = userHandler.getUser(currentUser.getIdUser());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", result);
            }
        });

        // === Back Button ===
        btnBack.setOnAction(e -> {
            // Kembali ke ProductWindow
            ProductWindow productWindow = new ProductWindow(stage, currentUser.getIdUser());
            stage.setScene(productWindow.createProductScene());
        });

        // === Layout ===
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER_LEFT);
        
        root.getChildren().addAll(
            lblTitle, 
            lblName, tfName, 
            lblEmail, tfEmail, 
            lblPass, pfPass, 
            lblPhone, tfPhone, 
            lblAddress, taAddress, 
            new Separator(), 
            btnUpdate, btnBack
        );

        return new Scene(root, 400, 600);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}