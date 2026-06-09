import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableViewCollectionsApp extends Application {
    private final ObservableList<Student> students = FXCollections.observableArrayList(
            new Student("Aarav", "JavaFX", 88),
            new Student("Sima", "JDBC", 81),
            new Student("Nabin", "Networking", 76),
            new Student("Prisha", "Multithreading", 91)
    );

    @Override
    public void start(Stage stage) {
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or topic");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        ComboBox<String> topicBox = new ComboBox<>();
        topicBox.getItems().addAll("JavaFX", "JDBC", "Networking", "Servlets/JSP", "Multithreading");
        topicBox.setPromptText("Topic");

        TextField marksField = new TextField();
        marksField.setPromptText("Marks");
        marksField.setPrefWidth(90);

        TableView<Student> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());

        TableColumn<Student, String> topicColumn = new TableColumn<>("Topic");
        topicColumn.setCellValueFactory(cell -> cell.getValue().topicProperty());

        TableColumn<Student, Number> marksColumn = new TableColumn<>("Marks");
        marksColumn.setCellValueFactory(cell -> cell.getValue().marksProperty());

        table.getColumns().add(nameColumn);
        table.getColumns().add(topicColumn);
        table.getColumns().add(marksColumn);

        FilteredList<Student> filteredStudents = new FilteredList<>(students, student -> true);
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String keyword = newText == null ? "" : newText.trim().toLowerCase();
            filteredStudents.setPredicate(student -> {
                if (keyword.isEmpty()) {
                    return true;
                }
                return student.getName().toLowerCase().contains(keyword)
                        || student.getTopic().toLowerCase().contains(keyword);
            });
        });

        SortedList<Student> sortedStudents = new SortedList<>(filteredStudents);
        sortedStudents.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedStudents);

        Label message = new Label("ObservableList updates the TableView when rows change.");

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            try {
                Student student = new Student(
                        nameField.getText().trim(),
                        topicBox.getValue(),
                        Integer.parseInt(marksField.getText().trim())
                );

                if (student.getName().isEmpty() || student.getTopic() == null) {
                    message.setText("Name and topic are required.");
                    return;
                }

                students.add(student);
                nameField.clear();
                topicBox.getSelectionModel().clearSelection();
                marksField.clear();
                message.setText("Added " + student.getName() + ".");
            } catch (NumberFormatException ex) {
                message.setText("Marks should be a number.");
            }
        });

        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(event -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                message.setText("Select a row first.");
                return;
            }
            students.remove(selected);
            message.setText("Removed " + selected.getName() + ".");
        });

        HBox inputRow = new HBox(10, nameField, topicBox, marksField, addButton, removeButton);
        VBox root = new VBox(12, searchField, table, inputRow, message);
        root.setPadding(new Insets(16));

        stage.setTitle("JavaFX TableView with Collections");
        stage.setScene(new Scene(root, 780, 440));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class Student {
        private final StringProperty name = new SimpleStringProperty();
        private final StringProperty topic = new SimpleStringProperty();
        private final IntegerProperty marks = new SimpleIntegerProperty();

        public Student(String name, String topic, int marks) {
            this.name.set(name);
            this.topic.set(topic);
            this.marks.set(marks);
        }

        public String getName() {
            return name.get();
        }

        public String getTopic() {
            return topic.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public StringProperty topicProperty() {
            return topic;
        }

        public IntegerProperty marksProperty() {
            return marks;
        }
    }
}
