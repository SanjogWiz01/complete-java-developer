import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set; // importing libries 

/*
 * This sample covers:
 * - Java class structure and imports
 * - Classes, objects, and constructors
 * - Data types
 * - Conditional statements
 * - Access modifiers
 * - Exception handling
 * - Java collections
 *
 * Compile:
 *   javac CoreJavaDemo.java
 *
 * Run:
 *   java CoreJavaDemo
 *
 * Classpath example:
 *   java -cp . CoreJavaDemo
 */
public class CoreJavaDemo {
    public static void main(String[] args) {
        System.out.println("Core Java Demo");
        System.out.println("==============");

        demonstrateDataTypes();
        demonstrateClassObjectConstructor();
        demonstrateConditionals(82);
        demonstrateExceptionHandling();
        demonstrateCollections();
    }

    private static void demonstrateDataTypes() {
        int age = 21;
        double fee = 1500.75;
        char grade = 'A';
        boolean active = true;
        String course = "Java";

        System.out.println("\nData Types");
        System.out.println("Age: " + age);
        System.out.println("Fee: " + fee);
        System.out.println("Grade: " + grade);
        System.out.println("Active: " + active);
        System.out.println("Course: " + course);
    }

    private static void demonstrateClassObjectConstructor() {
        Student student = new Student("Sanjog", 21);
        Course course = new Course("Java Developer", 12);

        System.out.println("\nClasses, Objects, and Constructors");
        student.enroll(course);
        student.printProfile();
    }

    private static void demonstrateConditionals(int marks) {
        System.out.println("\nConditional Statements");

        if (marks >= 80) {
            System.out.println("Result: Distinction");
        } else if (marks >= 60) {
            System.out.println("Result: First Division");
        } else if (marks >= 40) {
            System.out.println("Result: Pass");
        } else {
            System.out.println("Result: Fail");
        }

        int day = 2;
        switch (day) {
            case 1:
                System.out.println("Day: Sunday");
                break;
            case 2:
                System.out.println("Day: Monday");
                break;
            default:
                System.out.println("Day: Unknown");
                break;
        }
    }

    private static void demonstrateExceptionHandling() {
        System.out.println("\nException Handling");

        try {
            int parsedAge = parseAge("twenty");
            System.out.println("Parsed age: " + parsedAge);
        } catch (NumberFormatException exception) {
            System.out.println("Invalid number format: " + exception.getMessage());
        } finally {
            System.out.println("Age parsing completed.");
        }
    }

    private static int parseAge(String value) throws NumberFormatException {
        return Integer.parseInt(value);
    }

    private static void demonstrateCollections() {
        System.out.println("\nJava Collections");

        List<String> topics = new ArrayList<>();
        topics.add("Classes");
        topics.add("Packages");
        topics.add("Collections");

        Set<String> uniqueSkills = new HashSet<>();
        uniqueSkills.add("Java");
        uniqueSkills.add("SQL");
        uniqueSkills.add("Java");

        Map<String, Integer> marksBySubject = new HashMap<>();
        marksBySubject.put("Java", 82);
        marksBySubject.put("Database", 76);

        System.out.println("Topics: " + topics);
        System.out.println("Unique skills: " + uniqueSkills);
        System.out.println("Marks: " + marksBySubject);
    }
}

class Student {
    private final String name; // data abstractions
    private final int age;
    private Course course;

    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void enroll(Course course) {
        this.course = course;
    }

    public void printProfile() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);

        if (course != null) {
            System.out.println("Course: " + course.getTitle());
            System.out.println("Duration: " + course.getDurationInWeeks() + " weeks");
        }
    }
}

class Course {
    private final String title;
    private final int durationInWeeks;

    Course(String title, int durationInWeeks) {
        this.title = title;
        this.durationInWeeks = durationInWeeks;
    }

    public String getTitle() {
        return title;
    }

    protected int getDurationInWeeks() {
        return durationInWeeks;
    }
}
