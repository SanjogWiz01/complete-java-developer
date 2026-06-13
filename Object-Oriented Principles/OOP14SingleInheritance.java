public class OOP14SingleInheritance {
    /*
     * Single inheritance means one child class directly extends one parent
     * class. Java allows a class to extend only one class at a time.
     */
    static class Vehicle {
        void start() {
            System.out.println("Vehicle starts.");
        }
    }

    static class Motorcycle extends Vehicle {
        void balance() {
            System.out.println("Motorcycle balances on two wheels.");
        }
    }

    public static void main(String[] args) {
        Motorcycle motorcycle = new Motorcycle();
        motorcycle.start();
        motorcycle.balance();
    }
}
