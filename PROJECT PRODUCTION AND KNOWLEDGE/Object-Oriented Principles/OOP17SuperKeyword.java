public class OOP17SuperKeyword {
    /*
     * super calls parent constructors, methods, or fields from a child class.
     */
    static class Employee {
        private final String name;

        Employee(String name) {
            this.name = name;
        }

        String description() {
            return "Employee: " + name;
        }
    }

    static class Developer extends Employee {
        private final String language;

        Developer(String name, String language) {
            super(name);
            this.language = language;
        }

        @Override
        String description() {
            return super.description() + ", writes " + language;
        }
    }

    public static void main(String[] args) {
        Developer developer = new Developer("Ravi", "Java");
        System.out.println(developer.description());
    }
}
