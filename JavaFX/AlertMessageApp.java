import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AlertMessageApp extends Application {
    @Override
    public void start(Stage stage) {
        Button button = new Button("Show Alert");
        button.setOnAction(event -> new Alert(Alert.AlertType.INFORMATION, "JavaFX alert message").showAndWait());

        stage.setScene(new Scene(new StackPane(button), 260, 130));
        stage.setTitle("Alert");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
