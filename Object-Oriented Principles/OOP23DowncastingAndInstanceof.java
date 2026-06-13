public class OOP23DowncastingAndInstanceof {
    /*
     * Downcasting converts a parent reference back to a child type. Check the
     * real object type with instanceof before downcasting.
     */
    static class Payment {
        void process() {
            System.out.println("Processing payment.");
        }
    }

    static class CardPayment extends Payment {
        void printCardLastFour() {
            System.out.println("Card ending with 4242.");
        }
    }

    public static void main(String[] args) {
        Payment payment = new CardPayment();
        payment.process();

        if (payment instanceof CardPayment cardPayment) {
            cardPayment.printCardLastFour();
        }
    }
}
