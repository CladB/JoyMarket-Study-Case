package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.UserWindow;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        UserWindow userWindow = new UserWindow(primaryStage);
        
        primaryStage.setTitle("JoyMarket System");
        // Mulai dari Register Scene sesuai permintaan Activity Diagram sebelumnya
        primaryStage.setScene(userWindow.createRegisterScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}