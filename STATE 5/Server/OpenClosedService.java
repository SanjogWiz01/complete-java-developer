package server;

import java.util.List;
import java.util.ArrayList;

public class OpenClosedService {

    public interface PaymentProcessor {
        boolean processPayment(double amount);
    }

    public static class CreditCardProcessor implements PaymentProcessor {
        @Override
        public boolean processPayment(double amount) {
            System.out.println("Processing credit card payment: $" + amount);
            return true;
        }
    }

    public static class PayPalProcessor implements PaymentProcessor {
        @Override
        public boolean processPayment(double amount) {
            System.out.println("Processing PayPal payment: $" + amount);
            return true;
        }
    }

    public static class CryptoProcessor implements PaymentProcessor {
        @Override
        public boolean processPayment(double amount) {
            System.out.println("Processing cryptocurrency payment: $" + amount);
            return true;
        }
    }

    public static class PaymentService {
        private final List<PaymentProcessor> processors = new ArrayList<>();

        public void addProcessor(PaymentProcessor processor) {
            processors.add(processor);
        }

        public void executePayment(double amount) {
            for (PaymentProcessor processor : processors) {
                if (processor.processPayment(amount)) {
                    System.out.println("Payment completed via " + processor.getClass().getSimpleName());
                    return;
                }
            }
            System.out.println("All payment methods failed.");
        }
    }

    public static void main(String[] args) {
        PaymentService service = new PaymentService();
        service.addProcessor(new CreditCardProcessor());
        service.addProcessor(new PayPalProcessor());
        service.addProcessor(new CryptoProcessor());
        service.executePayment(150.75);
    }
}
