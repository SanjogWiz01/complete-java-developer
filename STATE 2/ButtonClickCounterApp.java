import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ButtonClickCounterApp extends Application {
    private int clicks;

    @Override
    public void start(Stage stage) {
        Label label = new Label("Clicks: 0");
        Button button = new Button("Click Me");
        button.setOnAction(event -> label.setText("Clicks: " + ++clicks));

        VBox root = new VBox(10, label, button);
        stage.setScene(new Scene(root, 260, 140));
        stage.setTitle("Click Counter");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
