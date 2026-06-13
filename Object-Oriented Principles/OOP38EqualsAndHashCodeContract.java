import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OOP38EqualsAndHashCodeContract {
    /*
     * If two objects are equal according to equals, they must return the same
     * hashCode. This is required for collections like HashSet and HashMap.
     */
    static class StudentId {
        private final String value;

        StudentId(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof StudentId studentId)) {
                return false;
            }
            return Objects.equals(value, studentId.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static void main(String[] args) {
        Set<StudentId> ids = new HashSet<>();
        ids.add(new StudentId("S-101"));
        ids.add(new StudentId("S-101"));

        System.out.println("Unique IDs: " + ids.size());
    }
}
