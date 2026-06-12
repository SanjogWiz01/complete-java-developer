import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleVBoxLayoutApp extends Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.getChildren().addAll(
                new Button("Top"),
                new Button("Middle"),
                new Button("Bottom"));

        stage.setScene(new Scene(root, 220, 160));
        stage.setTitle("VBox Layout");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
