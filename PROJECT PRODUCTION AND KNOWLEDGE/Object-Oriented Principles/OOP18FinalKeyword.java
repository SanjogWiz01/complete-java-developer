public class OOP18FinalKeyword {
    /*
     * final variables cannot be reassigned, final methods cannot be overridden,
     * and final classes cannot be extended.
     */
    static class Receipt {
        private final String number;

        Receipt(String number) {
            this.number = number;
        }

        final String printHeader() {
            return "Receipt #" + number;
        }
    }

    static final class CashReceipt extends Receipt {
        CashReceipt(String number) {
            super(number);
        }
    }

    public static void main(String[] args) {
        final CashReceipt receipt = new CashReceipt("R-101");
        System.out.println(receipt.printHeader());
    }
}
