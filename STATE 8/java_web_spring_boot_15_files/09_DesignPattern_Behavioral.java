/*
 * 09_DesignPattern_Behavioral.java
 *
 * BEHAVIORAL DESIGN PATTERNS
 *
 * Examples:
 * - Strategy
 * - Observer
 * - Command
 */
package com.example.patterns;

import java.util.ArrayList;
import java.util.List;

public class BehavioralPatterns {

    // ---------------- STRATEGY ----------------
    interface DiscountStrategy {
        double apply(double price);
    }

    static class StudentDiscount implements DiscountStrategy {
        public double apply(double price) {
            return price * 0.90;
        }
    }

    static class NoDiscount implements DiscountStrategy {
        public double apply(double price) {
            return price;
        }
    }

    static class Checkout {
        private DiscountStrategy strategy;

        Checkout(DiscountStrategy strategy) {
            this.strategy = strategy;
        }

        double total(double price) {
            return strategy.apply(price);
        }
    }

    // ---------------- OBSERVER ----------------
    interface Observer {
        void update(String event);
    }

    static class EmailObserver implements Observer {
        public void update(String event) {
            System.out.println("Email notification: " + event);
        }
    }

    static class EventPublisher {
        private final List<Observer> observers = new ArrayList<>();

        void subscribe(Observer observer) {
            observers.add(observer);
        }

        void publish(String event) {
            for (Observer observer : observers) {
                observer.update(event);
            }
        }
    }

    // ---------------- COMMAND ----------------
    interface Command {
        void execute();
    }

    static class Light {
        void on() {
            System.out.println("Light ON");
        }
    }

    static class TurnOnLightCommand implements Command {
        private final Light light;

        TurnOnLightCommand(Light light) {
            this.light = light;
        }

        public void execute() {
            light.on();
        }
    }

    public static void main(String[] args) {
        Checkout checkout = new Checkout(new StudentDiscount());
        System.out.println(checkout.total(1000));

        EventPublisher publisher = new EventPublisher();
        publisher.subscribe(new EmailObserver());
        publisher.publish("Order created");

        Command command = new TurnOnLightCommand(new Light());
        command.execute();
    }
}
