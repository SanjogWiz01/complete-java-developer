/*
 * 08_DesignPattern_Structural.java
 *
 * STRUCTURAL DESIGN PATTERNS
 *
 * Examples:
 * - Adapter
 * - Decorator
 * - Facade
 */
package com.example.patterns;

public class StructuralPatterns {

    // ---------------- ADAPTER ----------------
    interface PaymentProcessor {
        void pay(double amount);
    }

    static class LegacyPaymentSystem {
        void makePaymentInNpr(double amount) {
            System.out.println("Legacy payment: NPR " + amount);
        }
    }

    static class PaymentAdapter implements PaymentProcessor {
        private final LegacyPaymentSystem legacy;

        PaymentAdapter(LegacyPaymentSystem legacy) {
            this.legacy = legacy;
        }

        @Override
        public void pay(double amount) {
            legacy.makePaymentInNpr(amount);
        }
    }

    // ---------------- DECORATOR ----------------
    interface Coffee {
        String description();
        double cost();
    }

    static class SimpleCoffee implements Coffee {
        public String description() {
            return "Coffee";
        }

        public double cost() {
            return 100;
        }
    }

    static abstract class CoffeeDecorator implements Coffee {
        protected final Coffee coffee;

        CoffeeDecorator(Coffee coffee) {
            this.coffee = coffee;
        }
    }

    static class MilkDecorator extends CoffeeDecorator {
        MilkDecorator(Coffee coffee) {
            super(coffee);
        }

        public String description() {
            return coffee.description() + " + Milk";
        }

        public double cost() {
            return coffee.cost() + 30;
        }
    }

    // ---------------- FACADE ----------------
    static class Inventory {
        boolean available() {
            return true;
        }
    }

    static class Payment {
        boolean pay(double amount) {
            return amount > 0;
        }
    }

    static class OrderFacade {
        private final Inventory inventory = new Inventory();
        private final Payment payment = new Payment();

        boolean placeOrder(double amount) {
            return inventory.available() && payment.pay(amount);
        }
    }

    public static void main(String[] args) {
        PaymentProcessor processor =
                new PaymentAdapter(new LegacyPaymentSystem());
        processor.pay(500);

        Coffee coffee = new MilkDecorator(new SimpleCoffee());
        System.out.println(coffee.description() + " = " + coffee.cost());

        System.out.println(new OrderFacade().placeOrder(1000));
    }
}
