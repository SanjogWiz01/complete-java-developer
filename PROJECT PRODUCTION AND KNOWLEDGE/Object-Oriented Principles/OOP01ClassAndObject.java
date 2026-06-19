public class OOP01ClassAndObject {
    /*
     * A class is the blueprint. An object is a runtime instance created from
     * that blueprint.
     */
    static class Student {
        String name;
        int rollNumber;

        void attendClass() {
            System.out.println(name + " is attending OOP class.");
        }
    }

    public static void main(String[] args) {
        Student firstStudent = new Student();
        firstStudent.name = "Anita";
        firstStudent.rollNumber = 7;

        System.out.println("Object state: " + firstStudent.name + ", roll " + firstStudent.rollNumber);
        firstStudent.attendClass();
    }
}
