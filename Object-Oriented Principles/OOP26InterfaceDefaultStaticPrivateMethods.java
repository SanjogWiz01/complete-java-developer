public class OOP26InterfaceDefaultStaticPrivateMethods {
    /*
     * Modern interfaces can have abstract, default, static, and private helper
     * methods. Default methods provide reusable behavior to implementers.
     */
    interface Auditable {
        void saveAuditEvent(String message);

        default void auditCreate(String entityName) {
            saveAuditEvent(format("Created " + entityName));
        }

        static String category() {
            return "AUDIT";
        }

        private String format(String message) {
            return "[" + category() + "] " + message;
        }
    }

    static class ConsoleAuditor implements Auditable {
        @Override
        public void saveAuditEvent(String message) {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        Auditable auditor = new ConsoleAuditor();
        auditor.auditCreate("Student");
    }
}
