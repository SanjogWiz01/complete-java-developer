package com.jyotibank.presentation;

import java.util.Scanner;

public class MainMenu {

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n=== Jyoti Bank Main Menu ===");
                System.out.println("1. Admin Portal");
                System.out.println("2. Customer Portal");
                System.out.println("3. Reports");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                String option = scanner.nextLine().trim();

                switch (option) {
                    case "1" -> new AdminUI().show();
                    case "2" -> new CustomerUI().show();
                    case "3" -> new ReportUI().show();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            }
        }
    }
}
