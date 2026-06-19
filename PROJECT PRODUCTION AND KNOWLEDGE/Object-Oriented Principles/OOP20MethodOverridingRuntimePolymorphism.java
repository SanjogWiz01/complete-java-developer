public class OOP20MethodOverridingRuntimePolymorphism {
    /*
     * Method overriding replaces parent behavior in a child class. Runtime
     * polymorphism chooses the method based on the actual object type.
     */
    static class Notification {
        void send() {
            System.out.println("Sending generic notification.");
        }
    }

    static class EmailNotification extends Notification {
        @Override
        void send() {
            System.out.println("Sending email notification.");
        }
    }

    static class SmsNotification extends Notification {
        @Override
        void send() {
            System.out.println("Sending SMS notification.");
        }
    }

    public static void main(String[] args) {
        Notification[] notifications = {
                new EmailNotification(),
                new SmsNotification()
        };

        for (Notification notification : notifications) {
            notification.send();
        }
    }
}
