import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloStageApp extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello JavaFX Stage");
        Scene scene = new Scene(label, 300, 120);

        stage.setTitle("Hello Stage");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
