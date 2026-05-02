class UpcastingVsDowncasting {
    static class Employee {
        void work() {
            System.out.println("Employee is working.");
        }
    }

    static class Developer extends Employee {
        @Override
        void work() {
            System.out.println("Developer is writing code.");
        }

        void debug() {
            System.out.println("Developer is debugging.");
        }
    }

    public static void main(String[] args) {
        Employee emp = new Developer(); // upcasting
        emp.work();

        if (emp instanceof Developer) {
            Developer dev = (Developer) emp; // downcasting
            dev.debug();
        }
    }
}
