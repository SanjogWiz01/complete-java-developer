import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProgressBarDemoApp extends Application {
    @Override
    public void start(Stage stage) {
        ProgressBar progressBar = new ProgressBar(0.4);
        Slider slider = new Slider(0, 1, 0.4);
        progressBar.progressProperty().bind(slider.valueProperty());

        stage.setScene(new Scene(new VBox(12, progressBar, slider), 280, 130));
        stage.setTitle("ProgressBar");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
