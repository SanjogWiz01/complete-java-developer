import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckBoxChoiceApp extends Application {
    @Override
    public void start(Stage stage) {
        CheckBox checkBox = new CheckBox("Enable notifications");
        Label status = new Label("Notifications off");

        checkBox.setOnAction(event -> {
            String message = checkBox.isSelected() ? "Notifications on" : "Notifications off";
            status.setText(message);
        });

        stage.setScene(new Scene(new VBox(10, checkBox, status), 280, 130));
        stage.setTitle("CheckBox");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
