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
                        "The primary Stage is provided by the runtime, and extra windows can be created when needed.")
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
