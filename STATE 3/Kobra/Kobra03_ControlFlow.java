public class Kobra03_ControlFlow {
    public static void main(String[] args) {
        int score = 85;

        if (score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 75) {
            System.out.println("Grade: B");
        } else if (score >= 60) {
            System.out.println("Grade: C");
        } else {
            System.out.println("Grade: F");
        }

        String day = "Monday";
        switch (day) {
            case "Monday" -> System.out.println("Start of the week!");
            case "Friday" -> System.out.println("Almost weekend!");
            default -> System.out.println("Regular day.");
        }
    }
}
