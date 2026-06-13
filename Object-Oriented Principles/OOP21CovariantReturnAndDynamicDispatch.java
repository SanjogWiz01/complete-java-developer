public class OOP21CovariantReturnAndDynamicDispatch {
    /*
     * A child override may return a more specific type. Dynamic dispatch still
     * chooses the overriding method at runtime.
     */
    static class Document {
        Document copy() {
            return new Document();
        }

        String type() {
            return "document";
        }
    }

    static class Invoice extends Document {
        @Override
        Invoice copy() {
            return new Invoice();
        }

        @Override
        String type() {
            return "invoice";
        }
    }

    public static void main(String[] args) {
        Document document = new Invoice();
        Document copy = document.copy();

        System.out.println("Copied type: " + copy.type());
    }
}
