public class OOP27MultipleInheritanceWithInterfaces {
    /*
     * Java does not allow a class to extend multiple classes, but it can
     * implement multiple interfaces. This avoids many state-sharing conflicts.
     */
    interface Flyable {
        void fly();
    }

    interface Trackable {
        void track();
    }

    static class DeliveryDrone implements Flyable, Trackable {
        @Override
        public void fly() {
            System.out.println("Drone is flying.");
        }

        @Override
        public void track() {
            System.out.println("Drone location is tracked.");
        }
    }

    public static void main(String[] args) {
        DeliveryDrone drone = new DeliveryDrone();
        drone.fly();
        drone.track();
    }
}
