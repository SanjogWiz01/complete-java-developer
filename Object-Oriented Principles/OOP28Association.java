public class OOP28Association {
    /*
     * Association means one object knows about or uses another object. The
     * objects can still exist independently.
     */
    static class Teacher {
        private final String name;

        Teacher(String name) {
            this.name = name;
        }

        void teach(Student student) {
            System.out.println(name + " teaches " + student.name + ".");
        }
    }

    static class Student {
        private final String name;

        Student(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Teacher teacher = new Teacher("Mr. Sharma");
        Student student = new Student("Nisha");

        teacher.teach(student);
    }
}
