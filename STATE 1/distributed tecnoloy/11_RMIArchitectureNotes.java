public class RMIArchitectureNotes {
    public static void main(String[] args) {
        System.out.println("RMI Architecture:");
        System.out.println("1. Client invokes methods on remote objects.");
        System.out.println("2. Stub/proxy transfers call details over network.");
        System.out.println("3. Remote Reference Layer manages remote object references.");
        System.out.println("4. Transport Layer uses TCP to communicate.");
        System.out.println("5. Server-side implementation executes and returns result.");
    }
}
