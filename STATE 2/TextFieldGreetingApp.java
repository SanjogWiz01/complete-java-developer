import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TextFieldGreetingApp extends Application {
    @Override
    public void start(Stage stage) {
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Label greeting = new Label("Welcome");
        Button button = new Button("Greet");
        button.setOnAction(event -> greeting.setText("Hello, " + nameField.getText()));

        VBox root = new VBox(10, nameField, button, greeting);
        stage.setScene(new Scene(root, 280, 160));
        stage.setTitle("Greeting");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
