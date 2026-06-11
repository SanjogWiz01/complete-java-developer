public class JavaOopSummary {
    public static void main(String[] args) {
        printSummary();
        Student student = new Student("Asha", 20);
        student.printDetails();
    }

    private static void printSummary() {
        String[] points = {
                "Object-oriented programming models data and behavior together.",
                "A class is a blueprint, and an object is an instance of that class.",
                "Encapsulation protects fields by controlling access through methods.",
                "Inheritance lets one class reuse and extend another class.",
                "Polymorphism lets the same method call behave differently for different objects."
        };

        for (String point : points) {
            System.out.println("- " + point);
        }
    }

    private static class Student {
        private final String name;
        private final int age;

        private Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        private void printDetails() {
            System.out.println(name + " is " + age + " years old.");
        }
    }
}
