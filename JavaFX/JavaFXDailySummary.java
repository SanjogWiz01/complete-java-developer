import java.util.List;

public class JavaFXDailySummary {
    public static void main(String[] args) {
        for (Section section : sections()) {
            section.print();
        }
    }

    private static List<Section> sections() {
        return List.of(
                section(
                        "Application",
                        "A JavaFX program usually starts from a class that extends Application.",
                        "The start method receives the primary Stage and builds the first scene."),
                section(
                        "Stage",
                        "A Stage is the top-level window shown by a JavaFX application.",
                        "The primary Stage is provided by the runtime, and extra windows can be created when needed."),
                section(
                        "Scene",
                        "A Scene holds the visual content inside a Stage.",
                        "Switching scenes is a common way to move between screens such as login, dashboard, and settings."),
                section(
                        "Layout Panes",
                        "Layout panes arrange child nodes without manual pixel positioning.",
                        "VBox, HBox, BorderPane, GridPane, and StackPane solve different layout problems."),
                section(
                        "Controls",
                        "Controls are ready-made UI components for user interaction.",
                        "Button, Label, TextField, CheckBox, ComboBox, TableView, and ListView are common examples."),
                section(
                        "Event Handling",
                        "Events describe user actions such as clicking, typing, selecting, and dragging.",
                        "Handlers are attached to controls so the application can respond to those actions."),
                section(
                        "Properties and Binding",
                        "JavaFX properties wrap values and support change observation.",
                        "Binding keeps one value automatically synchronized with another value or expression."),
                section(
                        "CSS Styling",
                        "JavaFX supports CSS so visual styling can be separated from application logic.",
                        "Style classes and external stylesheets keep screens easier to maintain.")
        );
    }

    private static Section section(String title, String... notes) {
        return new Section(title, List.of(notes));
    }

    private record Section(String title, List<String> notes) {
        private void print() {
            System.out.println(title);
            for (String note : notes) {
                System.out.println("- " + note);
            }
            System.out.println();
        }
    }
}
