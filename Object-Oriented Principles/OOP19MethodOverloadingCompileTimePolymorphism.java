public class OOP19MethodOverloadingCompileTimePolymorphism {
    /*
     * Method overloading means multiple methods share a name but have different
     * parameter lists. The compiler chooses the correct method at compile time.
     */
    static class Printer {
        void print(String value) {
            System.out.println("Text: " + value);
        }

        void print(int value) {
            System.out.println("Number: " + value);
        }

        void print(String label, int value) {
            System.out.println(label + ": " + value);
        }
    }

    public static void main(String[] args) {
        Printer printer = new Printer();
        printer.print("Java");
        printer.print(21);
        printer.print("Score", 95);
    }
}
