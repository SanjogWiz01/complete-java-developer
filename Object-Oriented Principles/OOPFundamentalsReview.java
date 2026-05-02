class OOPFundamentalsReview {
    static class Student {
        private final int id;
        private String name;
        private double gpa;

        Student(int id, String name, double gpa) {
            this.id = id;
            this.name = name;
            this.gpa = gpa;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getGpa() { return gpa; }
        public void setGpa(double gpa) { this.gpa = gpa; }

        public String describe() {
            return "Student{id=" + id + ", name='" + name + "', gpa=" + gpa + "}";
        }
    }

    public static void main(String[] args) {
        Student s1 = new Student(101, "Ava", 3.7);
        Student s2 = new Student(102, "Noah", 3.9);

        System.out.println("=== OOP Fundamentals Review ===");
        System.out.println(s1.describe());
        System.out.println(s2.describe());

        s1.setGpa(3.8);
        System.out.println("Updated: " + s1.getName() + " GPA -> " + s1.getGpa());
    }
}
