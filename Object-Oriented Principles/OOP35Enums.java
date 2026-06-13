public class OOP35Enums {
    /*
     * Enums model a fixed set of constants. They can have fields, constructors,
     * and methods like normal classes.
     */
    enum OrderStatus {
        NEW("Created"),
        PAID("Payment received"),
        SHIPPED("On the way"),
        DELIVERED("Completed");

        private final String label;

        OrderStatus(String label) {
            this.label = label;
        }

        String label() {
            return label;
        }
    }

    public static void main(String[] args) {
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println(status + " -> " + status.label());
        }
    }
}
