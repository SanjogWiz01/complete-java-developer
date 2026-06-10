import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundTaskThreadingApp extends Application {
    private final ExecutorService worker = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "advanced-java-worker");
        thread.setDaemon(true);
        return thread;
    });

    private Task<Void> runningTask;

    @Override
    public void start(Stage stage) {
        Label status = new Label("Ready to run a background report.");
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(360);

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(10);

        Button startButton = new Button("Start Task");
        Button cancelButton = new Button("Cancel");
        cancelButton.setDisable(true);

        startButton.setOnAction(event -> {
            runningTask = createReportTask();

            status.textProperty().bind(runningTask.messageProperty());
            progressBar.progressProperty().bind(runningTask.progressProperty());

            runningTask.messageProperty().addListener((obs, oldMessage, newMessage) -> {
                if (newMessage != null && !newMessage.isBlank()) {
                    logArea.appendText(newMessage + System.lineSeparator());
                }
            });

            runningTask.setOnSucceeded(done -> {
                finishTask(startButton, cancelButton, status, progressBar);
                status.setText("Report finished. The UI stayed responsive.");
            });

            runningTask.setOnCancelled(done -> {
                finishTask(startButton, cancelButton, status, progressBar);
                status.setText("Task cancelled by user.");
            });

            runningTask.setOnFailed(done -> {
                finishTask(startButton, cancelButton, status, progressBar);
                status.setText("Task failed: " + runningTask.getException().getMessage());
            });

            startButton.setDisable(true);
            cancelButton.setDisable(false);
            logArea.clear();
            worker.submit(runningTask); // comments 
        });

        cancelButton.setOnAction(event -> {
            if (runningTask != null) {
                runningTask.cancel();
            }
        });

        HBox buttons = new HBox(10, startButton, cancelButton);
        VBox root = new VBox(12, status, progressBar, buttons, logArea);
        root.setPadding(new Insets(16));

        stage.setTitle("JavaFX Task and Multithreading");
        stage.setScene(new Scene(root, 560, 360));
        stage.show();
    }

    private Task<Void> createReportTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                int totalSteps = 8;

                for (int step = 1; step <= totalSteps; step++) {
                    if (isCancelled()) {
                        updateMessage("Stopping at step " + step + ".");
                        return null;
                    }

                    Thread.sleep(450);
                    updateProgress(step, totalSteps);
                    updateMessage("Processed advanced Java topic " + step + " of " + totalSteps + ".");
                }

                return null;
            }
        };
    }

    private void finishTask(Button startButton, Button cancelButton, Label status, ProgressBar progressBar) {
        startButton.setDisable(false);
        cancelButton.setDisable(true);
        status.textProperty().unbind();
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
    }

    @Override
    // Shut down the worker thread when the JavaFX window closes.
    public void stop() {
        worker.shutdownNow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
