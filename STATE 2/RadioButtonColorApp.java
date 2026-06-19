import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RadioButtonColorApp extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("Choose a color");
        RadioButton red = new RadioButton("Red");
        RadioButton green = new RadioButton("Green");
        RadioButton blue = new RadioButton("Blue");

        ToggleGroup group = new ToggleGroup();
        red.setToggleGroup(group);
        green.setToggleGroup(group);
        blue.setToggleGroup(group);

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == red) {
                label.setStyle("-fx-text-fill: red;");
            } else if (newToggle == green) {
                label.setStyle("-fx-text-fill: green;");
            } else if (newToggle == blue) {
                label.setStyle("-fx-text-fill: blue;");
            }
        });

        stage.setScene(new Scene(new VBox(8, label, red, green, blue), 260, 170));
        stage.setTitle("Radio Buttons");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
