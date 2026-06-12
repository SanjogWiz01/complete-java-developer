import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SimpleHBoxLayoutApp extends Application {
    @Override
    public void start(Stage stage) {
        HBox root = new HBox(10);
        root.getChildren().addAll(
                new Button("Left"),
                new Button("Center"),
                new Button("Right"));

        stage.setScene(new Scene(root, 320, 100));
        stage.setTitle("HBox Layout");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
