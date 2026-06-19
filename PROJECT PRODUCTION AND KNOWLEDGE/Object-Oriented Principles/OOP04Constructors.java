public class OOP04Constructors {
    /*
     * Constructors initialize new objects. They have the same name as the class
     * and do not declare a return type.
     */
    static class Course {
        private final String code;
        private final String title;

        Course(String code, String title) {
            this.code = code;
            this.title = title;
        }

        String label() {
            return code + " - " + title;
        }
    }

    public static void main(String[] args) {
        Course course = new Course("JAV201", "Object-Oriented Programming");
        System.out.println(course.label());
    }
}
