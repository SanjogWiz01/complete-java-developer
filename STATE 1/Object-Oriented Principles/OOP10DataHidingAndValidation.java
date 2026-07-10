public class OOP10DataHidingAndValidation {
    /*
     * Data hiding keeps fields private. Validation protects object invariants,
     * meaning the rules that should always remain true for an object.
     */
    static class ExamScore {
        private int marks;

        ExamScore(int marks) {
            setMarks(marks);
        }

        void setMarks(int marks) {
            if (marks < 0 || marks > 100) {
                throw new IllegalArgumentException("Marks must be from 0 to 100.");
            }
            this.marks = marks;
        }

        int getMarks() {
            return marks;
        }

        boolean passed() {
            return marks >= 40;
        }
    }

    public static void main(String[] args) {
        ExamScore score = new ExamScore(82);
        System.out.println("Marks: " + score.getMarks());
        System.out.println("Passed: " + score.passed());
    }
}
