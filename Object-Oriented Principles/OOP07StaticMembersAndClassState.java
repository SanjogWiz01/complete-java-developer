public class OOP07StaticMembersAndClassState {
    /*
     * Instance members belong to one object. Static members belong to the class
     * itself and are shared by all objects.
     */
    static class Ticket {
        private static int nextNumber = 1000;
        private final int number;

        Ticket() {
            number = nextNumber++;
        }

        static int nextTicketNumber() {
            return nextNumber;
        }

        int number() {
            return number;
        }
    }

    public static void main(String[] args) {
        Ticket first = new Ticket();
        Ticket second = new Ticket();

        System.out.println("Issued: " + first.number());
        System.out.println("Issued: " + second.number());
        System.out.println("Next static number: " + Ticket.nextTicketNumber());
    }
}
