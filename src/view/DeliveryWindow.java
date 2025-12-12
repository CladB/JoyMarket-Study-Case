package view;

import controller.CourierHandler;
import controller.DeliveryHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Courier;
import model.OrderHeader;
import java.util.List;

public class DeliveryWindow {

	private DeliveryHandler deliveryHandler = new DeliveryHandler();
	private CourierHandler courierHandler = new CourierHandler();

	// === Pop up Form === 
	public void showAssignCourierWindow(OrderHeader order) {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL); 
		popupStage.setTitle("Assign Courier");

		Label lblTitle = new Label("Assign Courier for Order: " + order.getIdOrder());
		lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

		Label lblCustomer = new Label("Customer: " + order.getCustomerName());

		// === Assign Courier (choose) ===
		Label lblSelect = new Label("Select Courier:");
		ComboBox<Courier> cmbCourier = new ComboBox<>();

		// === Fetch data courier ===
		List<Courier> couriers = courierHandler.getAllCouriers();
		cmbCourier.getItems().addAll(couriers);

		// === ComboBox show -> Nama kurir, bukan object hash code ===
		cmbCourier.setCellFactory(param -> new ListCell<Courier>() {
			@Override
			protected void updateItem(Courier item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null)
					setText(null);
				else
					setText(item.getName() + " (" + item.getVehicleType() + ")");
			}
		});
		cmbCourier.setButtonCell(cmbCourier.getCellFactory().call(null)); // Interface saat dipilih

		// === Action Button === 
		Button btnAssign = new Button("Assign Now");
		Button btnCancel = new Button("Cancel");

		btnAssign.setOnAction(e -> {
			Courier selectedCourier = cmbCourier.getValue();
			if (selectedCourier != null) {
				String result = deliveryHandler.assignCourier(order.getIdOrder(), selectedCourier.getIdCourier());

				if (result.startsWith("Success")) {
					showAlert(Alert.AlertType.INFORMATION, "Success", result);
					popupStage.close(); // close popup kalau sudah berhasil
				} else {
					showAlert(Alert.AlertType.ERROR, "Error", result);
				}
			} else {
				showAlert(Alert.AlertType.WARNING, "Warning", "Please select a courier first.");
			}
		});

		btnCancel.setOnAction(e -> popupStage.close());

		// Layout
		VBox layout = new VBox(15);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER_LEFT);
		layout.getChildren().addAll(lblTitle, lblCustomer, new Separator(), lblSelect, cmbCourier, new Separator(),
				btnAssign, btnCancel);

		Scene scene = new Scene(layout, 350, 300);
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