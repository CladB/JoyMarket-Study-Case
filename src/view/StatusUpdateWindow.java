package view;

import controller.DeliveryHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.OrderHeader;

public class StatusUpdateWindow {
	// Popup kecil untuk kurir supaya bisa memilih status baru
	private DeliveryHandler deliveryHandler = new DeliveryHandler();

	public void showUpdateStatus(OrderHeader order) {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Update Status");

		Label lblTitle = new Label("Update Order: " + order.getIdOrder());
		lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

		// === Edit Status ===
		Label lblStatus = new Label("Select New Status:");
		ComboBox<String> cmbStatus = new ComboBox<>();
		// === Pilihan status sesuai keadaan oleh courier ===
		cmbStatus.getItems().addAll("Pending", "On Process", "Delivered", "Cancelled");

		// === Set default value sesuai status saat ===
		cmbStatus.setValue(order.getStatus());

		Button btnUpdate = new Button("Update Status");
		Button btnCancel = new Button("Cancel");

		btnUpdate.setOnAction(e -> {
			String newStatus = cmbStatus.getValue();
			if (newStatus != null) {
				String result = deliveryHandler.editDeliveryStatus(order.getIdOrder(), newStatus);

				if (result.startsWith("Success")) {
					showAlert(Alert.AlertType.INFORMATION, "Success", result);
					popupStage.close();
				} else {
					showAlert(Alert.AlertType.ERROR, "Error", result);
				}
			}
		});

		btnCancel.setOnAction(e -> popupStage.close());

		// === Layout ===
		VBox layout = new VBox(15);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.getChildren().addAll(lblTitle, lblStatus, cmbStatus, new Separator(), btnUpdate, btnCancel);

		Scene scene = new Scene(layout, 300, 250);
		popupStage.setScene(scene);
		popupStage.showAndWait();
	}

	private void showAlert(Alert.AlertType type, String title, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.show();
	}
}