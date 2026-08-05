package server;

public class DependencyInversionService {

    public interface MessageSender {
        void sendMessage(String message, String recipient);
    }

    public static class EmailSender implements MessageSender {
        @Override
        public void sendMessage(String message, String recipient) {
            System.out.println("Sending email to " + recipient + ": " + message);
        }
    }

    public static class SmsSender implements MessageSender {
        @Override
        public void sendMessage(String message, String recipient) {
            System.out.println("Sending SMS to " + recipient + ": " + message);
        }
    }

    public static class NotificationService {
        private final MessageSender messageSender;

        public NotificationService(MessageSender messageSender) {
            this.messageSender = messageSender;
        }

        public void notify(String message, String recipient) {
            messageSender.sendMessage(message, recipient);
        }
    }

    public static void main(String[] args) {
        NotificationService emailNotify = new NotificationService(new EmailSender());
        emailNotify.notify("Your order has been shipped.", "user@example.com");

        NotificationService smsNotify = new NotificationService(new SmsSender());
        smsNotify.notify("Your OTP is 123456.", "+1234567890");
    }
}
