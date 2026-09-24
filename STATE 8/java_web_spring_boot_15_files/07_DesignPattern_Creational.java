/*
 * 07_DesignPattern_Creational.java
 *
 * CREATIONAL DESIGN PATTERNS
 *
 * Focus:
 * - Singleton
 * - Factory Method
 * - Builder
 *
 * Design patterns are reusable design approaches, not copy-paste code.
 */
package com.example.patterns;

public class CreationalPatterns {

    // ---------------- SINGLETON ----------------
    static final class AppConfig {
        private static final AppConfig INSTANCE = new AppConfig();

        private AppConfig() {
        }

        public static AppConfig getInstance() {
            return INSTANCE;
        }

        public String getEnvironment() {
            return "development";
        }
    }

    // ---------------- FACTORY ----------------
    interface Notification {
        void send(String message);
    }

    static final class EmailNotification implements Notification {
        @Override
        public void send(String message) {
            System.out.println("Email: " + message);
        }
    }

    static final class SmsNotification implements Notification {
        @Override
        public void send(String message) {
            System.out.println("SMS: " + message);
        }
    }

    static final class NotificationFactory {
        public static Notification create(String type) {
            return switch (type.toLowerCase()) {
                case "email" -> new EmailNotification();
                case "sms" -> new SmsNotification();
                default -> throw new IllegalArgumentException("Unknown type");
            };
        }
    }

    // ---------------- BUILDER ----------------
    static final class User {
        private final String name;
        private final String email;
        private final int age;

        private User(Builder builder) {
            this.name = builder.name;
            this.email = builder.email;
            this.age = builder.age;
        }

        public static class Builder {
            private String name;
            private String email;
            private int age;

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder email(String email) {
                this.email = email;
                return this;
            }

            public Builder age(int age) {
                this.age = age;
                return this;
            }

            public User build() {
                return new User(this);
            }
        }

        @Override
        public String toString() {
            return name + " | " + email + " | " + age;
        }
    }

    public static void main(String[] args) {
        System.out.println(AppConfig.getInstance().getEnvironment());

        Notification notification = NotificationFactory.create("email");
        notification.send("Welcome");

        User user = new User.Builder()
                .name("Sanjog")
                .email("student@example.com")
                .age(21)
                .build();

        System.out.println(user);
    }
}
