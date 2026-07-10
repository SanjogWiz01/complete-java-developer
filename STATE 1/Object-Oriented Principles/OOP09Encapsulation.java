public class OOP09Encapsulation {
    /*
     * Encapsulation bundles data with behavior and exposes a controlled public
     * API instead of allowing every field to be modified directly.
     */
    static class Wallet {
        private int cents;

        Wallet(int startingCents) {
            if (startingCents < 0) {
                throw new IllegalArgumentException("Starting money cannot be negative.");
            }
            cents = startingCents;
        }

        void add(int cents) {
            if (cents <= 0) {
                throw new IllegalArgumentException("Add amount must be positive.");
            }
            this.cents += cents;
        }

        boolean spend(int cents) {
            if (cents <= 0 || cents > this.cents) {
                return false;
            }
            this.cents -= cents;
            return true;
        }

        int balanceInCents() {
            return cents;
        }
    }

    public static void main(String[] args) {
        Wallet wallet = new Wallet(500);
        wallet.add(250);
        wallet.spend(300);

        System.out.println("Wallet cents: " + wallet.balanceInCents());
    }
}
