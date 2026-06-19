import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BorderPaneDemoApp extends Application {
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setTop(new Label("Top menu"));
        root.setCenter(new Label("Main content"));
        root.setBottom(new Label("Status bar"));

        stage.setScene(new Scene(root, 320, 180));
        stage.setTitle("BorderPane");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
