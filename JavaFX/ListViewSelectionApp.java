import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListViewSelectionApp extends Application {
    @Override
    public void start(Stage stage) {
        ListView<String> topics = new ListView<>();
        topics.getItems().addAll("Stage", "Scene", "Layout", "Control");
        topics.setPrefHeight(120);

        Label selected = new Label("Pick a topic");
        topics.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> selected.setText("Topic: " + newValue));

        stage.setScene(new Scene(new VBox(10, topics, selected), 280, 190));
        stage.setTitle("ListView");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
