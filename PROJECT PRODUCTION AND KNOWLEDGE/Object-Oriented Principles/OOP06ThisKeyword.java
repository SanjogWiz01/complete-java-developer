public class OOP06ThisKeyword {
    /*
     * this refers to the current object. It is useful when field names and
     * parameter names are the same, and when returning the current object.
     */
    static class StringBuilderStyleName {
        private String firstName;
        private String lastName;

        StringBuilderStyleName firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        StringBuilderStyleName lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        String fullName() {
            return this.firstName + " " + this.lastName;
        }
    }

    public static void main(String[] args) {
        StringBuilderStyleName name = new StringBuilderStyleName()
                .firstName("Sanjog")
                .lastName("Wiz");

        System.out.println(name.fullName());
    }
}
