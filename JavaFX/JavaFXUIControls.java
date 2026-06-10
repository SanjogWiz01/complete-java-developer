public import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFXUIControls extends Application {

    @Override
    public void start(Stage stage) {

        Label label = new Label("Enter Name:");

        TextField textField = new TextField();

        Button button = new Button("Submit");

        CheckBox checkBox = new CheckBox("Accept Terms");

        RadioButton radioButton = new RadioButton("Male");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Java", "Python", "C++");

        VBox root = new VBox(10);

        root.getChildren().addAll(
                label,
                textField,
                button,
                checkBox,
                radioButton,
                comboBox
        );

        Scene scene = new Scene(root, 300, 250);

        stage.setTitle("JavaFX UI Controls");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} {
    
}
