public class OOP30Composition {
    /*
     * Composition is a strong whole-part relationship. The whole owns the part
     * and usually controls its lifecycle.
     */
    static class Engine {
        void ignite() {
            System.out.println("Engine ignites.");
        }
    }

    static class Car {
        private final Engine engine = new Engine();

        void start() {
            engine.ignite();
            System.out.println("Car starts.");
        }
    }

    public static void main(String[] args) {
        Car car = new Car();
        car.start();
    }
}
