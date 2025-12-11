package view;

import controller.UserHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class UserWindow {

	private UserHandler userHandler;
	private Stage stage;

	public UserWindow(Stage stage) {
		this.stage = stage;
		this.userHandler = new UserHandler();
	}

	// ==========================================
	// SCENE 1: REGISTER PAGE (Sesuai Diagram 1)
	// ==========================================
	public Scene createRegisterScene() {
		// 1. Komponen UI
		Label lblTitle = new Label("JoyMarket Register");
		lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		TextField tfName = new TextField();
		tfName.setPromptText("Full Name");

		TextField tfEmail = new TextField();
		tfEmail.setPromptText("Email");

		PasswordField pfPassword = new PasswordField();
		pfPassword.setPromptText("Password");

		TextField tfPhone = new TextField();
		tfPhone.setPromptText("Phone Number");

		TextArea taAddress = new TextArea();
		taAddress.setPromptText("Address");
		taAddress.setPrefHeight(60);

		Button btnRegister = new Button("Register Account");
		Button btnGoToLogin = new Button("Already have account? Login");

		// 2. Event Handling
		btnRegister.setOnAction(e -> {
			String name = tfName.getText();
			String email = tfEmail.getText();
			String password = pfPassword.getText();
			String phone = tfPhone.getText();
			String address = taAddress.getText();

			// Panggil Controller
			String result = userHandler.saveDataUser(name, email, password, phone, address);

			if (result.equals("Success")) {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Successful!", "Please login.");
				stage.setScene(createLoginScene()); // Pindah ke Login
			} else {
				showAlert(Alert.AlertType.ERROR, "Failed", "Error", result);
			}
		});

		// Tombol Navigasi ke Login
		btnGoToLogin.setOnAction(e -> {
			stage.setScene(createLoginScene());
		});

		// 3. Layout (VBox) - PASTIKAN BAGIAN INI ADA
		VBox root = new VBox(10); // Jarak antar elemen 10px
		root.setPadding(new Insets(20)); // Jarak dari pinggir window 20px
		root.setAlignment(Pos.CENTER);

		// MENAMBAHKAN SEMUA KOMPONEN KE LAYOUT
		root.getChildren().addAll(lblTitle, new Label("Name:"), tfName, new Label("Email:"), tfEmail,
				new Label("Password:"), pfPassword, new Label("Phone:"), tfPhone, new Label("Address:"), taAddress,
				btnRegister, btnGoToLogin);

		return new Scene(root, 400, 600);
	}

	// ==========================================
	// SCENE 2: LOGIN PAGE (Sesuai Diagram 2)
	// ==========================================
	public Scene createLoginScene() {
		Label lblTitle = new Label("JoyMarket Login");
		lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		TextField tfEmail = new TextField();
		tfEmail.setPromptText("Email");

		PasswordField pfPassword = new PasswordField();
		pfPassword.setPromptText("Password");

		Button btnLogin = new Button("Login");
		Button btnBack = new Button("Back to Register");

		// Action Login
		btnLogin.setOnAction(e -> {
			String email = tfEmail.getText();
			String pass = pfPassword.getText();

			// Panggil Handler Login
			User user = userHandler.login(email, pass);

			if (user != null) {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Welcome", "Login Berhasil!");

				// Pindah ke ProductWindow dengan membawa ID User
				ProductWindow productWindow = new ProductWindow(stage, user.getIdUser());
				stage.setScene(productWindow.createProductScene());

			} else {
				showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", "Email atau Password salah.");
			}
		});

		// Action Back
		btnBack.setOnAction(e -> {
			stage.setScene(createRegisterScene());
		});

		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);

		// MENAMBAHKAN KOMPONEN LOGIN KE LAYOUT
		root.getChildren().addAll(lblTitle, new Label("Email:"), tfEmail, new Label("Password:"), pfPassword, btnLogin,
				btnBack);

		return new Scene(root, 400, 400);
	}

	// Helper untuk Alert
	private void showAlert(Alert.AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}