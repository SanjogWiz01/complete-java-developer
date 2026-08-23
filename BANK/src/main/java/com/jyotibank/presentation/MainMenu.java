package com.jyotibank.presentation;

import com.jyotibank.service.AccountService;
import com.jyotibank.service.AuthService;
import com.jyotibank.service.CustomerService;
import com.jyotibank.service.FixedDepositService;
import com.jyotibank.service.InterestService;
import com.jyotibank.service.ReportService;
import com.jyotibank.service.TransferService;
import com.jyotibank.service.TransactionService;

import java.util.Optional;

public class MainMenu {

    private final AuthService authService;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransferService transferService;
    private final TransactionService transactionService;
    private final FixedDepositService fixedDepositService;
    private final InterestService interestService;
    private final ReportService reportService;

    public MainMenu(AuthService authService,
                    CustomerService customerService,
                    AccountService accountService,
                    TransferService transferService,
                    TransactionService transactionService,
                    FixedDepositService fixedDepositService,
                    InterestService interestService,
                    ReportService reportService) {
        this.authService = authService;
        this.customerService = customerService;
        this.accountService = accountService;
        this.transferService = transferService;
        this.transactionService = transactionService;
        this.fixedDepositService = fixedDepositService;
        this.interestService = interestService;
        this.reportService = reportService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            ConsoleIO.header("JYOTI BANK — WELCOME");
            System.out.println("1. Login");
            System.out.println("0. Exit");
            String option = ConsoleIO.readLine("Choose an option: ");

            switch (option) {
                case "1" -> routeToPortal();
                case "0" -> running = false;
                default -> ConsoleIO.error("Invalid option.");
            }
        }
        System.out.println("Thank you for banking with Jyoti Bank. Goodbye!");
    }

    private void routeToPortal() {
        Optional<SessionContext> session = new LoginUI(authService).login();
        if (session.isEmpty()) return;

        boolean keepGoing = true;
        while (keepGoing) {
            if (session.get().isAdmin()) {
                AdminUI adminUI = new AdminUI(session.get(), authService, customerService,
                        accountService, fixedDepositService, interestService, reportService);
                keepGoing = adminUI.show();
            } else {
                CustomerUI customerUI = new CustomerUI(session.get(), authService, customerService, accountService,
                        transferService, transactionService, fixedDepositService);
                keepGoing = customerUI.show();
            }
        }
    }
}
