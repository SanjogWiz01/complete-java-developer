import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Kobra08_CollectionsDemo {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Kobra");
        names.add("Java");
        names.add("Spring");
        names.add("Kobra");

        System.out.println("List: " + names);
        System.out.println("Contains 'Java'? " + names.contains("Java"));

        Set<String> uniqueNames = new HashSet<>(names);
        System.out.println("Set (unique): " + uniqueNames);

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Kobra", 95);
        scores.put("Java", 88);
        scores.put("Spring", 92);

        System.out.println("Map:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("Score of Java: " + scores.getOrDefault("Java", 0));
    }
}
