class MethodOverridingOverloading {
    static class Calculator {
        int add(int a, int b) { // overloading #1
            return a + b;
        }

        double add(double a, double b) { // overloading #2
            return a + b;
        }
    }

    static class ScientificCalculator extends Calculator {
        @Override
        int add(int a, int b) { // overriding parent method
            System.out.println("Overridden add(int, int) called");
            return super.add(a, b);
        }
    }

    public static void main(String[] args) {
        Calculator base = new Calculator();
        ScientificCalculator sci = new ScientificCalculator();

        System.out.println("Base add int: " + base.add(3, 4));
        System.out.println("Base add double: " + base.add(3.5, 4.2));
        System.out.println("Scientific add int: " + sci.add(10, 20));
    }
}
