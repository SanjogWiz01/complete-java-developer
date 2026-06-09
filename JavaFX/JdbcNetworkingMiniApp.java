import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcNetworkingMiniApp extends Application {
    private final StudentDao studentDao = new MemoryStudentDao();

    @Override
    public void start(Stage stage) {
        TabPane tabs = new TabPane();
        tabs.getTabs().add(new Tab("JDBC CRUD", buildJdbcTab()));
        tabs.getTabs().add(new Tab("Socket Client", buildSocketTab()));
        tabs.getTabs().forEach(tab -> tab.setClosable(false));

        stage.setTitle("JavaFX with JDBC and Networking");
        stage.setScene(new Scene(tabs, 760, 470));
        stage.show();
    }

    private VBox buildJdbcTab() {
        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField topicField = new TextField();
        topicField.setPromptText("Topic");

        TextArea output = new TextArea();
        output.setEditable(false);

        Button saveButton = new Button("Save");
        Button findButton = new Button("Find");
        Button listButton = new Button("List All");
        Button deleteButton = new Button("Delete");

        saveButton.setOnAction(event -> {
            Optional<Student> student = readStudent(idField, nameField, topicField, output);
            student.ifPresent(value -> {
                studentDao.save(value);
                output.setText("Saved student through DAO:\n" + value);
            });
        });

        findButton.setOnAction(event -> {
            int id = readId(idField, output);
            if (id < 0) {
                return;
            }

            Optional<Student> found = studentDao.findById(id);
            output.setText(found.map(Student::toString).orElse("No student found for id " + id + "."));
        });

        listButton.setOnAction(event -> output.setText(formatStudents(studentDao.findAll())));

        deleteButton.setOnAction(event -> {
            int id = readId(idField, output);
            if (id < 0) {
                return;
            }
            studentDao.deleteById(id);
            output.setText("Deleted id " + id + " if it existed.");
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("ID"), idField);
        form.addRow(1, new Label("Name"), nameField);
        form.addRow(2, new Label("Topic"), topicField);

        HBox buttons = new HBox(10, saveButton, findButton, listButton, deleteButton);

        Label note = new Label("The app uses memory storage now, but the DAO shape is ready for real JDBC.");
        note.setWrapText(true);

        VBox root = new VBox(12, note, form, buttons, output);
        root.setPadding(new Insets(16));
        return root;
    }

    private VBox buildSocketTab() {
        TextField hostField = new TextField("localhost");
        TextField portField = new TextField("8080");
        TextField messageField = new TextField("hello from JavaFX client");

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setText("Socket programming connects client code with a server through host and port.");

        Button sendButton = new Button("Send Message");
        sendButton.setOnAction(event -> {
            int port = readPort(portField, output);
            if (port < 0) {
                return;
            }

            Task<String> socketTask = createSocketTask(hostField.getText().trim(), port, messageField.getText());
            sendButton.disableProperty().bind(socketTask.runningProperty());
            socketTask.setOnSucceeded(done -> {
                sendButton.disableProperty().unbind();
                sendButton.setDisable(false);
                output.setText(socketTask.getValue());
            });
            socketTask.setOnFailed(done -> {
                sendButton.disableProperty().unbind();
                sendButton.setDisable(false);
                output.setText("Could not connect: " + socketTask.getException().getMessage());
            });

            Thread thread = new Thread(socketTask, "socket-client-demo");
            thread.setDaemon(true);
            thread.start();
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Host"), hostField);
        form.addRow(1, new Label("Port"), portField);
        form.addRow(2, new Label("Message"), messageField);

        VBox root = new VBox(12, form, sendButton, output);
        root.setPadding(new Insets(16));
        return root;
    }

    private Optional<Student> readStudent(TextField idField, TextField nameField, TextField topicField, TextArea output) {
        int id = readId(idField, output);
        if (id < 0) {
            return Optional.empty();
        }

        String name = nameField.getText().trim();
        String topic = topicField.getText().trim();
        if (name.isEmpty() || topic.isEmpty()) {
            output.setText("Name and topic are required.");
            return Optional.empty();
        }

        return Optional.of(new Student(id, name, topic));
    }

    private int readId(TextField idField, TextArea output) {
        try {
            return Integer.parseInt(idField.getText().trim());
        } catch (NumberFormatException ex) {
            output.setText("ID should be a number.");
            return -1;
        }
    }

    private int readPort(TextField portField, TextArea output) {
        try {
            return Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException ex) {
            output.setText("Port should be a number.");
            return -1;
        }
    }

    private String formatStudents(List<Student> students) {
        if (students.isEmpty()) {
            return "No students saved yet.";
        }

        StringBuilder builder = new StringBuilder();
        for (Student student : students) {
            builder.append(student).append(System.lineSeparator());
        }
        return builder.toString();
    }

    private Task<String> createSocketTask(String host, int port, String message) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), 1500);
                    socket.setSoTimeout(1500);

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                    writer.write(message);
                    writer.newLine();
                    writer.flush();

                    String reply = reader.readLine();
                    return "Sent: " + message + System.lineSeparator()
                            + "Server reply: " + (reply == null ? "(connection closed)" : reply);
                }
            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }

    private interface StudentDao {
        void save(Student student);

        Optional<Student> findById(int id);

        List<Student> findAll();

        void deleteById(int id);
    }

    private static class MemoryStudentDao implements StudentDao {
        private final List<Student> rows = new ArrayList<>();

        @Override
        public void save(Student student) {
            deleteById(student.id);
            rows.add(student);
        }

        @Override
        public Optional<Student> findById(int id) {
            return rows.stream()
                    .filter(student -> student.id == id)
                    .findFirst();
        }

        @Override
        public List<Student> findAll() {
            return new ArrayList<>(rows);
        }

        @Override
        public void deleteById(int id) {
            rows.removeIf(student -> student.id == id);
        }
    }

    private static class JdbcStudentDao implements StudentDao {
        private final Connection connection;

        private JdbcStudentDao(Connection connection) throws SQLException {
            this.connection = connection;
            createTableIfNeeded();
        }

        private void createTableIfNeeded() throws SQLException {
            String sql = "CREATE TABLE IF NOT EXISTS students ("
                    + "id INTEGER PRIMARY KEY, "
                    + "name VARCHAR(80), "
                    + "topic VARCHAR(80))";

            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
        }

        @Override
        public void save(Student student) {
            deleteById(student.id);
            String sql = "INSERT INTO students (id, name, topic) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, student.id);
                statement.setString(2, student.name);
                statement.setString(3, student.topic);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new IllegalStateException("Could not save student", ex);
            }
        }

        @Override
        public Optional<Student> findById(int id) {
            String sql = "SELECT id, name, topic FROM students WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(toStudent(resultSet));
                    }
                }
            } catch (SQLException ex) {
                throw new IllegalStateException("Could not find student", ex);
            }
            return Optional.empty();
        }

        @Override
        public List<Student> findAll() {
            String sql = "SELECT id, name, topic FROM students ORDER BY id";
            List<Student> students = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(toStudent(resultSet));
                }
            } catch (SQLException ex) {
                throw new IllegalStateException("Could not list students", ex);
            }

            return students;
        }

        @Override
        public void deleteById(int id) {
            String sql = "DELETE FROM students WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new IllegalStateException("Could not delete student", ex);
            }
        }

        private Student toStudent(ResultSet resultSet) throws SQLException {
            return new Student(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("topic")
            );
        }
    }

    private static class Student {
        private final int id;
        private final String name;
        private final String topic;

        private Student(int id, String name, String topic) {
            this.id = id;
            this.name = name;
            this.topic = topic;
        }

        @Override
        public String toString() {
            return id + " - " + name + " (" + topic + ")";
        }
    }
}
