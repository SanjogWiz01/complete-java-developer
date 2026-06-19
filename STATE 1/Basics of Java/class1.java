import java.util.Scanner; // java import for I/O
public class class1 {
    public static void main(String[] args) {
        // Variables
        String name;
        int age;
        int marks;

        Scanner sc = new Scanner(System.in);

        // Input
        System.out.print("Enter your name: ");
        name = sc.nextLine();
        System.out.print("Enter your age: ");
        age = sc.nextInt();
        System.out.print("Enter your marks: ");
        marks = sc.nextInt();

        // DISPLAY
        System.out.println("--- Student Information ---");
        System.out.println("STUDENT NAME = " + name + " AGE = " + age + " MARKS = " + marks);

        if (marks >= 80) {
            System.out.println("Grade: A");
        } else if (marks >= 60) {
            System.out.println("Grade: B");
        } else if (marks >= 40) {
            System.out.println("Grade: C");
        } else {
            System.out.println("Grade: Fail");
        }

        sc.close();
    }
}
