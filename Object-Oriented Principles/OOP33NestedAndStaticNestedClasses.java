public class OOP33NestedAndStaticNestedClasses {
    /*
     * A static nested class belongs to the outer class but does not need an
     * outer object. It is useful for grouping helper types.
     */
    static class Invoice {
        private final String number;
        private final Money amount;

        Invoice(String number, Money amount) {
            this.number = number;
            this.amount = amount;
        }

        String summary() {
            return number + " = " + amount.format();
        }

        static class Money {
            private final String currency;
            private final int wholeUnits;

            Money(String currency, int wholeUnits) {
                this.currency = currency;
                this.wholeUnits = wholeUnits;
            }

            String format() {
                return currency + " " + wholeUnits;
            }
        }
    }

    public static void main(String[] args) {
        Invoice.Money money = new Invoice.Money("NPR", 1500);
        Invoice invoice = new Invoice("INV-1", money);
        System.out.println(invoice.summary());
    }
}
