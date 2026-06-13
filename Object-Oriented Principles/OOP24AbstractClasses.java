public class OOP24AbstractClasses {
    /*
     * Abstract classes cannot be instantiated. They can contain shared fields,
     * concrete methods, and abstract methods that children must implement.
     */
    abstract static class Report {
        private final String title;

        Report(String title) {
            this.title = title;
        }

        String header() {
            return "Report: " + title;
        }

        abstract String body();
    }

    static class SalesReport extends Report {
        SalesReport() {
            super("Sales");
        }

        @Override
        String body() {
            return "Total sales increased this month.";
        }
    }

    public static void main(String[] args) {
        Report report = new SalesReport();
        System.out.println(report.header());
        System.out.println(report.body());
    }
}
