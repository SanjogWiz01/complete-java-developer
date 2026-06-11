import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaCollectionsSummary {
    public static void main(String[] args) {
        printSummary();
        showExample();
    }

    private static void printSummary() {
        String[] points = {
                "Collections store groups of objects and can grow or shrink at runtime.",
                "List keeps elements in order and allows duplicates.",
                "Set stores unique elements.",
                "Map stores key-value pairs.",
                "Generics make collections type-safe, such as List<String>."
        };

        for (String point : points) {
            System.out.println("- " + point);
        }
    }

    private static void showExample() {
        List<String> languages = new ArrayList<>();
        languages.add("Java");
        languages.add("SQL");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Java", 95);
        scores.put("SQL", 88);

        System.out.println("Languages: " + languages);
        System.out.println("Java score: " + scores.get("Java"));
    }
}
