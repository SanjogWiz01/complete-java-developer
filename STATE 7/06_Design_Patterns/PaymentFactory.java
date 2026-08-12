package unit7.patterns;

interface Payment {
    void pay(double amount);
}

class CardPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Paid Rs." + amount + " using card");
    }
}

class WalletPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Paid Rs." + amount + " using digital wallet");
    }
}

class CashOnDelivery implements Payment {
    public void pay(double amount) {
        System.out.println("Order Rs." + amount + " marked for cash on delivery");
    }
}

class PaymentFactory {
    public static Payment create(String method) {
        return switch (method.toLowerCase()) {
            case "card" -> new CardPayment();
            case "wallet" -> new WalletPayment();
            case "cod" -> new CashOnDelivery();
            default -> throw new IllegalArgumentException("Unsupported payment: " + method);
        };
    }
}

public class PaymentFactoryDemo {
    public static void main(String[] args) {
        Payment payment = PaymentFactory.create("wallet");
        payment.pay(2500);
    }
}
