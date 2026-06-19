import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EventHandlingFormApp extends Application {
    @Override
    public void start(Stage stage) {
        TextField nameField = new TextField();
        nameField.setPromptText("Student name");

        TextField emailField = new TextField();
        emailField.setPromptText("student@example.com");

        ComboBox<String> semesterBox = new ComboBox<>();
        semesterBox.getItems().addAll("Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth");
        semesterBox.setPromptText("Choose semester");

        ToggleGroup levelGroup = new ToggleGroup();
        RadioButton beginner = new RadioButton("Beginner");
        RadioButton comfortable = new RadioButton("Comfortable");
        beginner.setToggleGroup(levelGroup);
        comfortable.setToggleGroup(levelGroup);
        beginner.setSelected(true);

        CheckBox javafx = new CheckBox("JavaFX");
        CheckBox jdbc = new CheckBox("JDBC");
        CheckBox networking = new CheckBox("Networking");

        Label message = new Label("Fill the form, then save one student record.");
        ListView<String> savedList = new ListView<>();
        savedList.setPrefHeight(150);

        Button saveButton = new Button("Save");
        Button clearButton = new Button("Clear");

        saveButton.setOnAction(event -> {
            String error = findFormError(nameField, emailField, semesterBox);
            if (!error.isEmpty()) {
                message.setText(error);
                return;
            }

            String selectedLevel = ((RadioButton) levelGroup.getSelectedToggle()).getText();
            String interests = buildInterestText(javafx, jdbc, networking);
            String row = nameField.getText().trim() + " | " + semesterBox.getValue()
                    + " | " + selectedLevel + " | " + interests;

            savedList.getItems().add(row);
            message.setText("Saved. Event handling worked without refreshing the whole screen.");
        });

        clearButton.setOnAction(event -> {
            nameField.clear();
            emailField.clear();
            semesterBox.getSelectionModel().clearSelection();
            beginner.setSelected(true);
            javafx.setSelected(false);
            jdbc.setSelected(false);
            networking.setSelected(false);
            message.setText("Form cleared.");
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(16));
        form.addRow(0, new Label("Name"), nameField);
        form.addRow(1, new Label("Email"), emailField);
        form.addRow(2, new Label("Semester"), semesterBox);
        form.addRow(3, new Label("Level"), new HBox(12, beginner, comfortable));
        form.addRow(4, new Label("Topics"), new HBox(12, javafx, jdbc, networking));

        HBox buttons = new HBox(10, saveButton, clearButton);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(12, form, buttons, message, savedList);
        root.setPadding(new Insets(16));

        stage.setTitle("JavaFX Event Handling and Form Validation");
        stage.setScene(new Scene(root, 650, 430));
        stage.show();
    }

    private String findFormError(TextField nameField, TextField emailField, ComboBox<String> semesterBox) {
        if (nameField.getText().trim().isEmpty()) {
            return "Name is required.";
        }
        if (!emailField.getText().contains("@")) {
            return "Email should contain @.";
        }
        if (semesterBox.getValue() == null) {
            return "Please choose a semester.";
        }
        return "";
    }

    private String buildInterestText(CheckBox javafx, CheckBox jdbc, CheckBox networking) {
        StringBuilder text = new StringBuilder();

        if (javafx.isSelected()) {
            text.append("JavaFX ");
        }
        if (jdbc.isSelected()) {
            text.append("JDBC ");
        }
        if (networking.isSelected()) {
            text.append("Networking ");
        }

        return text.length() == 0 ? "General Java" : text.toString().trim();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
