public class RMIVsCORBA {
    public static void main(String[] args) {
        System.out.println("CORBA Architecture (short):");
        System.out.println("CORBA uses ORB middleware, IDL contracts, and Naming Service.");
        System.out.println();
        System.out.println("RMI vs CORBA");
        System.out.println("-------------------------------");
        System.out.println("RMI: Java-to-Java distributed apps.");
        System.out.println("CORBA: Language-independent distributed apps.");
        System.out.println("RMI uses Java interfaces and serialization.");
        System.out.println("CORBA uses IDL and ORB middleware.");
        System.out.println("RMI is simpler for pure Java systems.");
        System.out.println("CORBA is broader for mixed-language systems.");
    }
}
