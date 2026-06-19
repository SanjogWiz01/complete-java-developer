import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneLoginApp extends Application {
    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        grid.add(new Label("Username:"), 0, 0);
        grid.add(new TextField(), 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(new PasswordField(), 1, 1);
        grid.add(new Button("Login"), 1, 2);

        stage.setScene(new Scene(grid, 320, 150));
        stage.setTitle("GridPane Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
