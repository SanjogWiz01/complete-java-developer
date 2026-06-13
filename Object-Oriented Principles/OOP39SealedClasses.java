public class OOP39SealedClasses {
    /*
     * Sealed classes and interfaces restrict which classes may extend or
     * implement them. This is useful when the allowed subtype list is known.
     */
    sealed interface Delivery permits BikeDelivery, VanDelivery {
        int estimatedMinutes();
    }

    static final class BikeDelivery implements Delivery {
        @Override
        public int estimatedMinutes() {
            return 25;
        }
    }

    static non-sealed class VanDelivery implements Delivery {
        @Override
        public int estimatedMinutes() {
            return 45;
        }
    }

    public static void main(String[] args) {
        Delivery delivery = new BikeDelivery();
        System.out.println("ETA minutes: " + delivery.estimatedMinutes());
    }
}
