public class OOP32CompositionOverInheritance {
    /*
     * Composition over inheritance means building behavior by combining small
     * objects instead of creating deep inheritance trees.
     */
    interface DiscountPolicy {
        double discount(double amount);
    }

    static class FestivalDiscount implements DiscountPolicy {
        @Override
        public double discount(double amount) {
            return amount * 0.10;
        }
    }

    static class Bill {
        private final DiscountPolicy discountPolicy;

        Bill(DiscountPolicy discountPolicy) {
            this.discountPolicy = discountPolicy;
        }

        double totalAfterDiscount(double amount) {
            return amount - discountPolicy.discount(amount);
        }
    }

    public static void main(String[] args) {
        Bill bill = new Bill(new FestivalDiscount());
        System.out.println("Payable: " + bill.totalAfterDiscount(2000));
    }
}
