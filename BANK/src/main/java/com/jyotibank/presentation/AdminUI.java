package com.jyotibank.presentation;

import com.jyotibank.model.Account;
import com.jyotibank.model.Customer;
import com.jyotibank.service.AccountService;
import com.jyotibank.service.AuthService;
import com.jyotibank.service.CustomerService;
import com.jyotibank.service.FixedDepositService;
import com.jyotibank.service.InterestService;
import com.jyotibank.service.ReportService;
import com.jyotibank.util.DateUtil;

import java.math.BigDecimal;
import java.util.List;

public class AdminUI {

    private final SessionContext session;
    private final AuthService authService;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final FixedDepositService fixedDepositService;
    private final InterestService interestService;
    private final ReportService reportService;

    public AdminUI(SessionContext session,
                   AuthService authService,
                   CustomerService customerService,
                   AccountService accountService,
                   FixedDepositService fixedDepositService,
                   InterestService interestService,
                   ReportService reportService) {
        this.session = session;
        this.authService = authService;
        this.customerService = customerService;
        this.accountService = accountService;
        this.fixedDepositService = fixedDepositService;
        this.interestService = interestService;
        this.reportService = reportService;
    }

    /** Runs the admin loop. Returns false when the admin logs out. */
    public boolean show() {
        boolean loggedIn = true;
        while (loggedIn) {
            if (sessionExpired()) return false;

            ConsoleIO.header("ADMIN PORTAL — " + session.getUser().getUsername());
            System.out.println(" 1. Register customer");
            System.out.println(" 2. List customers");
            System.out.println(" 3. Update customer contact info");
            System.out.println(" 4. Open savings account");
            System.out.println(" 5. Open current account");
            System.out.println(" 6. View accounts of a customer");
            System.out.println(" 7. View account details");
            System.out.println(" 8. Block / unblock / close account");
            System.out.println(" 9. Apply monthly interest");
            System.out.println("10. Bank reports");
            System.out.println(" 0. Logout");

            String option = ConsoleIO.readLine("Choose an option: ");
            try {
                switch (option) {
                    case "1" -> registerCustomer();
                    case "2" -> listCustomers();
                    case "3" -> updateCustomer();
                    case "4" -> openAccount(true);
                    case "5" -> openAccount(false);
                    case "6" -> viewCustomerAccounts();
                    case "7" -> viewAccountDetails();
                    case "8" -> manageAccountStatus();
                    case "9" -> applyInterest();
                    case "10" -> new ReportUI(session, reportService, fixedDepositService).show();
                    case "0" -> {
                        authService.logout(session.getUser());
                        ConsoleIO.info("Logged out.");
                        loggedIn = false;
                    }
                    default -> ConsoleIO.error("Invalid option.");
                }
            } catch (Exception e) {
                ConsoleIO.error(e.getMessage());
            }
            session.touch();
        }
        return false;
    }

    // ── Workflows ────────────────────────────────────────────────────────

    private void registerCustomer() {
        ConsoleIO.header("REGISTER CUSTOMER");
        Customer customer = new Customer();
        customer.setFirstName(ConsoleIO.readNonEmpty("First name : "));
        customer.setLastName(ConsoleIO.readNonEmpty("Last name  : "));
        customer.setEmail(ConsoleIO.readNonEmpty("Email      : "));
        customer.setPhone(ConsoleIO.readNonEmpty("Phone      : "));
        customer.setNationalId(readOptional("National ID : "));
        customer.setCity(readOptional("City        : "));

        long customerId = customerService.createCustomer(customer);
        ConsoleIO.info("Customer created with ID " + customerId);

        if (ConsoleIO.confirm("Create a login account for this customer?")) {
            String username = ConsoleIO.readNonEmpty("Login username: ");
            String password = ConsoleIO.readNonEmpty("Login password: ");
            authService.registerCustomerUser(session.getUserId(), username, password, customerId);
            ConsoleIO.info("Login '" + username + "' created.");
        }
    }

    private void listCustomers() {
        ConsoleIO.header("CUSTOMERS");
        List<Customer> customers = customerService.listActiveCustomers();
        if (customers.isEmpty()) {
            ConsoleIO.info("(none)");
            return;
        }
        System.out.printf("  %-6s %-24s %-30s %-15s%n", "ID", "NAME", "EMAIL", "PHONE");
        for (Customer c : customers) {
            System.out.printf("  %-6d %-24s %-30s %-15s%n",
                    c.getCustomerId(), c.getFullName(), c.getEmail(), c.getPhone());
        }
    }

    private void updateCustomer() {
        long customerId = ConsoleIO.readLong("Customer ID to update: ");
        Customer customer = customerService.getCustomer(customerId);
        ConsoleIO.info("Editing " + customer.getFullName() + " (leave blank to keep current value)");

        String email = readOptional("New email [" + customer.getEmail() + "]: ");
        if (!email.isBlank()) customer.setEmail(email);
        String phone = readOptional("New phone [" + customer.getPhone() + "]: ");
        if (!phone.isBlank()) customer.setPhone(phone);
        String city = readOptional("New city [" + orDash(customer.getCity()) + "]: ");
        if (!city.isBlank()) customer.setCity(city);

        customerService.updateCustomer(customer);
        ConsoleIO.info("Customer updated.");
    }

    private void openAccount(boolean savings) {
        ConsoleIO.header(savings ? "OPEN SAVINGS ACCOUNT" : "OPEN CURRENT ACCOUNT");
        long customerId = ConsoleIO.readLong("Customer ID: ");
        BigDecimal deposit = ConsoleIO.readAmount("Opening deposit: ");

        Account account = savings
                ? accountService.openSavingsAccount(session.getUserId(), customerId, deposit)
                : accountService.openCurrentAccount(session.getUserId(), customerId, deposit);
        ConsoleIO.info("Account opened: " + account.getAccountNumber()
                + " (" + account.getAccountDescription() + ")");
    }

    private void viewCustomerAccounts() {
        long customerId = ConsoleIO.readLong("Customer ID: ");
        Customer customer = customerService.getCustomer(customerId);
        List<Account> accounts = accountService.getAccountsByCustomer(customerId);
        printAccounts(customer.getFullName(), accounts);
    }

    private void viewAccountDetails() {
        String number = ConsoleIO.readNonEmpty("Account number: ");
        Account account = accountService.getAccountByNumber(number);
        System.out.println("  Number     : " + account.getAccountNumber());
        System.out.println("  Type       : " + account.getAccountType());
        System.out.println("  Balance    : " + account.getBalance());
        System.out.println("  Status     : " + account.getStatus());
        System.out.println("  Min balance: " + account.getMinimumBalance());
        System.out.println("  Opened at  : " + DateUtil.format(account.getOpenedAt()));
    }

    private void manageAccountStatus() {
        ConsoleIO.header("ACCOUNT STATUS");
        System.out.println("1. Block   2. Unblock   3. Close");
        int choice = ConsoleIO.readInt("Action: ", 1, 3);
        String number = ConsoleIO.readNonEmpty("Account number: ");
        switch (choice) {
            case 1 -> accountService.blockAccount(session.getUserId(), number);
            case 2 -> accountService.unblockAccount(session.getUserId(), number);
            case 3 -> accountService.closeAccount(session.getUserId(), number);
            default -> { /* unreachable */ }
        }
        ConsoleIO.info("Done.");
    }

    private void applyInterest() {
        if (!ConsoleIO.confirm("Post monthly interest to all eligible accounts?")) return;
        InterestService.InterestRun run = interestService.applyMonthlyInterest(session.getUserId());
        ConsoleIO.info("Interest posted to " + run.accountsCredited()
                + " account(s); total " + run.totalInterestPosted());
    }

    // ── Shared helpers ───────────────────────────────────────────────────

    static void printAccounts(String ownerLabel, List<Account> accounts) {
        ConsoleIO.header("ACCOUNTS — " + ownerLabel);
        if (accounts.isEmpty()) {
            ConsoleIO.info("(none)");
            return;
        }
        System.out.printf("  %-20s %-14s %12s %-9s%n", "NUMBER", "TYPE", "BALANCE", "STATUS");
        for (Account a : accounts) {
            System.out.printf("  %-20s %-14s %12s %-9s%n",
                    a.getAccountNumber(), a.getAccountType(), a.getBalance(), a.getStatus());
        }
    }

    static String readOptional(String prompt) {
        return ConsoleIO.readLine(prompt);
    }

    static String orDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private boolean sessionExpired() {
        int timeout = com.jyotibank.config.AppConfig.getInstance()
                .getIntProperty("app.session.timeout.minutes", 30);
        if (session.isExpired(timeout)) {
            ConsoleIO.error("Session expired due to inactivity.");
            return true;
        }
        return false;
    }
}
