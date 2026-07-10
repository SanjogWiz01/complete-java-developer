public class OOP11ImmutableObjects {
    /*
     * Immutable objects cannot be changed after construction. Use final fields,
     * no setters, and return new objects when a value needs to change.
     */
    static final class Money {
        private final String currency;
        private final int cents;

        Money(String currency, int cents) {
            if (currency == null || currency.isBlank()) {
                throw new IllegalArgumentException("Currency is required.");
            }
            if (cents < 0) {
                throw new IllegalArgumentException("Amount cannot be negative.");
            }
            this.currency = currency;
            this.cents = cents;
        }

        Money plus(Money other) {
            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException("Currencies must match.");
            }
            return new Money(currency, cents + other.cents);
        }

        @Override
        public String toString() {
            return currency + " " + cents / 100 + "." + String.format("%02d", cents % 100);
        }
    }

    public static void main(String[] args) {
        Money total = new Money("NPR", 12500).plus(new Money("NPR", 750));
        System.out.println(total);
    }
}
