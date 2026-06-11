public class JavaControlFlowSummary {
    public static void main(String[] args) {
        printSummary();
        showExample(82);
    }

    private static void printSummary() {
        String[] points = {
                "Control flow decides which statements run and how many times they run.",
                "if, else if, and else are used for decisions.",
                "switch is useful when one value has several fixed cases.",
                "for, while, and do-while loops repeat code.",
                "break exits a loop or switch, while continue skips to the next loop step."
        };

        for (String point : points) {
            System.out.println("- " + point);
        }
    }

    private static void showExample(int marks) {
        if (marks >= 90) {
            System.out.println("Grade: A");
        } else if (marks >= 75) {
            System.out.println("Grade: B");
        } else if (marks >= 60) {
            System.out.println("Grade: C");
        } else {
            System.out.println("Grade: Needs improvement");
        }

        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.println("Practice attempt " + attempt);
        }
    }
}
