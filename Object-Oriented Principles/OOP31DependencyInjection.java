public class OOP31DependencyInjection {
    /*
     * Dependency injection passes required collaborators into an object instead
     * of constructing them internally. This reduces coupling and improves tests.
     */
    interface MessageSender {
        void send(String recipient, String message);
    }

    static class EmailSender implements MessageSender {
        @Override
        public void send(String recipient, String message) {
            System.out.println("Email to " + recipient + ": " + message);
        }
    }

    static class WelcomeService {
        private final MessageSender sender;

        WelcomeService(MessageSender sender) {
            this.sender = sender;
        }

        void welcome(String email) {
            sender.send(email, "Welcome to Java OOP.");
        }
    }

    public static void main(String[] args) {
        WelcomeService service = new WelcomeService(new EmailSender());
        service.welcome("student@example.com");
    }
}
