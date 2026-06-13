public class OOP02StateBehaviorIdentity {
    /*
     * Every object has state, behavior, and identity.
     * State: data held in fields.
     * Behavior: methods that perform work.
     * Identity: the specific object reference in memory.
     */
    static class BankAccount {
        private String owner;
        private double balance;

        BankAccount(String owner, double openingBalance) {
            this.owner = owner;
            this.balance = openingBalance;
        }

        void deposit(double amount) {
            balance += amount;
        }

        String summary() {
            return owner + " has balance " + balance;
        }
    }

    public static void main(String[] args) {
        BankAccount accountA = new BankAccount("Maya", 1000);
        BankAccount accountB = new BankAccount("Maya", 1000);

        accountA.deposit(250);

        System.out.println(accountA.summary());
        System.out.println(accountB.summary());
        System.out.println("Same identity? " + (accountA == accountB));
    }
}
