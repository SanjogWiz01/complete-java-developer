public class OOP25Interfaces {
    /*
     * Interfaces define capabilities. A class uses implements to promise that
     * it provides the interface methods.
     */
    interface Printable {
        void print();
    }

    interface Scannable {
        void scan();
    }

    static class MultiFunctionPrinter implements Printable, Scannable {
        @Override
        public void print() {
            System.out.println("Printing document.");
        }

        @Override
        public void scan() {
            System.out.println("Scanning document.");
        }
    }

    public static void main(String[] args) {
        MultiFunctionPrinter machine = new MultiFunctionPrinter();
        machine.print();
        machine.scan();
    }
}
