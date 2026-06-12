import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SliderFontSizeApp extends Application {
    @Override
    public void start(Stage stage) {
        Label text = new Label("Resize this text");
        Slider slider = new Slider(12, 36, 18);

        text.setStyle("-fx-font-size: 18px;");
        slider.valueProperty().addListener((obs, oldValue, newValue) ->
                text.setStyle("-fx-font-size: " + newValue.intValue() + "px;"));

        stage.setScene(new Scene(new VBox(12, text, slider), 300, 140));
        stage.setTitle("Slider");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
