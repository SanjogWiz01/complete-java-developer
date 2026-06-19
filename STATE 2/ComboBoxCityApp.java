import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ComboBoxCityApp extends Application {
    @Override
    public void start(Stage stage) {
        ComboBox<String> cities = new ComboBox<>();
        cities.getItems().addAll("Kathmandu", "Pokhara", "Lalitpur");
        cities.setPromptText("Select city");

        Label result = new Label("No city selected");
        cities.setOnAction(event -> result.setText("Selected: " + cities.getValue()));

        stage.setScene(new Scene(new VBox(10, cities, result), 280, 140));
        stage.setTitle("ComboBox");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
