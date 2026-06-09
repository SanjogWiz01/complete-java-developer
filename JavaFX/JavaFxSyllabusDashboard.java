import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFxSyllabusDashboard extends Application {
    private final ObservableList<Topic> topics = FXCollections.observableArrayList(
            new Topic(
                    "JavaFX Application Structure",
                    "Stage, Scene, layout pane, controls, and the start() method.",
                    "A JavaFX app starts from Application.launch(), then JavaFX calls start(Stage)."
            ),
            new Topic(
                    "Event Handling",
                    "Button clicks, text input, listeners, lambdas, and validation.",
                    "Events keep desktop apps interactive instead of only printing output in main()."
            ),
            new Topic(
                    "Collections and TableView",
                    "ObservableList, TableView, filtering, sorting, and model classes.",
                    "ObservableList tells JavaFX when data changes, so the screen can update automatically."
            ),
            new Topic(
                    "Threads and Tasks",
                    "Background work, Task, ProgressBar, and keeping the UI responsive.",
                    "Long work should not run on the JavaFX application thread."
            ),
            new Topic(
                    "JDBC and Networking",
                    "DAO pattern, PreparedStatement, sockets, and client-server flow.",
                    "The UI should call service classes, not hide database or socket logic inside button code."
            )
    );

    @Override
    public void start(Stage stage) {
        ListView<Topic> topicList = new ListView<>(topics);
        topicList.setPrefWidth(260);

        Label title = new Label("Advanced Java Syllabus Map");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label selectedTitle = new Label();
        selectedTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextArea notes = new TextArea();
        notes.setEditable(false);
        notes.setWrapText(true);
        notes.setPrefRowCount(12);

        VBox detailBox = new VBox(12, title, selectedTitle, notes);
        detailBox.setPadding(new Insets(18));

        BorderPane root = new BorderPane();
        root.setLeft(topicList);
        root.setCenter(detailBox);

        topicList.getSelectionModel().selectedItemProperty().addListener((obs, oldTopic, newTopic) -> {
            if (newTopic == null) {
                return;
            }

            selectedTitle.setText(newTopic.name);
            notes.setText(
                    "Main idea:\n" + newTopic.summary +
                            "\n\nClassroom note:\n" + newTopic.classNote +
                            "\n\nPractice task:\nWrite a small example and explain what part of the syllabus it covers."
            );
        });

        topicList.getSelectionModel().selectFirst();

        stage.setTitle("JavaFX - Advanced Java Syllabus Dashboard");
        stage.setScene(new Scene(root, 760, 420));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Topic {
        private final String name;
        private final String summary;
        private final String classNote;

        private Topic(String name, String summary, String classNote) {
            this.name = name;
            this.summary = summary;
            this.classNote = classNote;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
