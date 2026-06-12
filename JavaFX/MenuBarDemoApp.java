import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuBarDemoApp extends Application {
    @Override
    public void start(Stage stage) {
        Label message = new Label("Use the menu");
        MenuItem open = new MenuItem("Open");
        MenuItem exit = new MenuItem("Exit");
        open.setOnAction(event -> message.setText("Open clicked"));
        exit.setOnAction(event -> stage.close());

        Menu file = new Menu("File");
        file.getItems().addAll(open, exit);
        BorderPane root = new BorderPane(message);
        root.setTop(new MenuBar(file));

        stage.setScene(new Scene(root, 320, 160));
        stage.setTitle("MenuBar");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
