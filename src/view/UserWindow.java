package view;

import controller.UserHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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

	// === Register Page ===
	public Scene createRegisterScene() {
		Label lblTitle = new Label("JoyMarket Register");
		lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		TextField tfName = new TextField();
		tfName.setPromptText("Full Name");

		TextField tfEmail = new TextField();
		tfEmail.setPromptText("Email");

		PasswordField pfPassword = new PasswordField();
		pfPassword.setPromptText("Password");

		PasswordField pfConfirm = new PasswordField();
		pfConfirm.setPromptText("Retype Password");

		// === Gender Section ===
		Label lblGender = new Label("Gender:");
		ToggleGroup groupGender = new ToggleGroup();
		RadioButton rbMale = new RadioButton("Male");
		rbMale.setToggleGroup(groupGender);
		rbMale.setUserData("Male");
		RadioButton rbFemale = new RadioButton("Female");
		rbFemale.setToggleGroup(groupGender);
		rbFemale.setUserData("Female");

		HBox genderBox = new HBox(10, rbMale, rbFemale);
		genderBox.setAlignment(Pos.CENTER_LEFT);

		TextField tfPhone = new TextField();
		tfPhone.setPromptText("Phone Number");

		TextArea taAddress = new TextArea();
		taAddress.setPromptText("Address");
		taAddress.setPrefHeight(60);

		Button btnRegister = new Button("Register Account");
		Button btnGoToLogin = new Button("Already have account? Login");

		// === Event Handling ===
		btnRegister.setOnAction(e -> {
			String name = tfName.getText();
			String email = tfEmail.getText();
			String password = pfPassword.getText();
			String confirmPass = pfConfirm.getText();
			String phone = tfPhone.getText();
			String address = taAddress.getText();

			String gender = null;
			if (groupGender.getSelectedToggle() != null) {
				gender = groupGender.getSelectedToggle().getUserData().toString();
			}

			// === Validasi Confirm Password ===
			if (!password.equals(confirmPass)) {
				showAlert(Alert.AlertType.ERROR, "Validation Error", "Password Confirmation Error",
						"Password dan Confirm Password tidak cocok!");
				return; // === kalau berbeda Stop proses, tidak simpan ke database ===
			}

			// === Call Handler saveDataUser ===
			String result = userHandler.saveDataUser(name, email, password, gender, phone, address);

			if (result.equals("Success")) {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Successful!", "Please login.");
				stage.setScene(createLoginScene());
			} else {
				showAlert(Alert.AlertType.ERROR, "Failed", "Error", result);
			}
		});

		btnGoToLogin.setOnAction(e -> {
			stage.setScene(createLoginScene());
		});

		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);

		root.getChildren().addAll(lblTitle, new Label("Name:"), tfName, new Label("Email:"), tfEmail,
				new Label("Password:"), pfPassword, new Label("Confirm Password:"), pfConfirm, // <--- TAMBAHAN DI SINI
				lblGender, genderBox, new Label("Phone:"), tfPhone, new Label("Address:"), taAddress, btnRegister,
				btnGoToLogin);

		return new Scene(root, 400, 700);
	}

	// === Login Page ===
	public Scene createLoginScene() {
		Label lblTitle = new Label("JoyMarket Login");
		lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		TextField tfEmail = new TextField();
		tfEmail.setPromptText("Email");

		PasswordField pfPassword = new PasswordField();
		pfPassword.setPromptText("Password");

		Button btnLogin = new Button("Login");
		Button btnBack = new Button("Back to Register");

		btnLogin.setOnAction(e -> {
			String email = tfEmail.getText();
			String pass = pfPassword.getText();

			User user = userHandler.login(email, pass);

			if (user != null) {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Welcome",
						"Login Berhasil sebagai " + user.getRole());

				// === REDIRECT User by Role ===
				if (user.getRole().equalsIgnoreCase("Courier")) {
					// Jika Kurir -> Masuk ke Halaman Tugas Kurir
					CourierTaskWindow courierWindow = new CourierTaskWindow(stage, user);
					stage.setScene(courierWindow.createCourierScene());

				} else {
					// Jika Admin atau Customer -> Masuk ke ProductWindow
					ProductWindow productWindow = new ProductWindow(stage, user.getIdUser());
					stage.setScene(productWindow.createProductScene());
				}

			} else {
				showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", "Email atau Password salah.");
			}
		});

		btnBack.setOnAction(e -> {
			stage.setScene(createRegisterScene());
		});

		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);

		root.getChildren().addAll(lblTitle, new Label("Email:"), tfEmail, new Label("Password:"), pfPassword, btnLogin,
				btnBack);

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