package com.jyotibank.presentation;

import com.jyotibank.config.AppConfig;
import com.jyotibank.model.Account;
import com.jyotibank.model.Customer;
import com.jyotibank.model.Transaction;
import com.jyotibank.service.AccountService;
import com.jyotibank.service.CustomerService;
import com.jyotibank.service.FixedDepositService;
import com.jyotibank.service.TransferService;
import com.jyotibank.service.TransactionService;
import com.jyotibank.util.DateUtil;
import com.jyotibank.util.ReceiptPrinter;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class CustomerUI {

    private static final Path RECEIPTS_DIR = Path.of("receipts");

    private final SessionContext session;
    private final com.jyotibank.service.AuthService authService;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransferService transferService;
    private final TransactionService transactionService;
    private final FixedDepositService fixedDepositService;

    public CustomerUI(SessionContext session,
                      com.jyotibank.service.AuthService authService,
                      CustomerService customerService,
                      AccountService accountService,
                      TransferService transferService,
                      TransactionService transactionService,
                      FixedDepositService fixedDepositService) {
        this.session = session;
        this.authService = authService;
        this.customerService = customerService;
        this.accountService = accountService;
        this.transferService = transferService;
        this.transactionService = transactionService;
        this.fixedDepositService = fixedDepositService;
    }

    /** Runs the customer loop. Returns false when the customer logs out. */
    public boolean show() {
        boolean loggedIn = true;
        while (loggedIn) {
            if (sessionExpired()) return false;

            ConsoleIO.header("CUSTOMER PORTAL — " + session.getUser().getUsername());
            System.out.println("1. My profile");
            System.out.println("2. My accounts");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer money");
            System.out.println("6. Mini statement (last 10)");
            System.out.println("7. Statement by date range");
            System.out.println("8. Fixed deposits");
            System.out.println("0. Logout");

            String option = ConsoleIO.readLine("Choose an option: ");
            try {
                switch (option) {
                    case "1" -> showProfile();
                    case "2" -> showMyAccounts();
                    case "3" -> deposit();
                    case "4" -> withdraw();
                    case "5" -> transfer();
                    case "6" -> miniStatement();
                    case "7" -> rangedStatement();
                    case "8" -> new FixedDepositUI(session, fixedDepositService, accountService).show();
                    case "0" -> loggedIn = false;
                    default -> ConsoleIO.error("Invalid option.");
                }
            } catch (Exception e) {
                ConsoleIO.error(e.getMessage());
            }
            if (loggedIn) {
                session.touch();
            } else {
                logout();
            }
        }
        return false;
    }

    // ── Workflows ────────────────────────────────────────────────────────

    private void showProfile() {
        long customerId = requireCustomerId();
        Customer customer = customerService.getCustomer(customerId);
        ConsoleIO.header("MY PROFILE");
        System.out.println("  Name : " + customer.getFullName());
        System.out.println("  Email: " + customer.getEmail());
        System.out.println("  Phone: " + customer.getPhone());
        System.out.println("  City : " + AdminUI.orDash(customer.getCity()));
    }

    private void showMyAccounts() {
        List<Account> accounts = myAccounts();
        AdminUI.printAccounts("YOUR ACCOUNTS", accounts);
    }

    private void deposit() {
        String number = ConsoleIO.readNonEmpty("Account number: ");
        requireOwnership(number);
        BigDecimal amount = ConsoleIO.readAmount("Amount to deposit: ");

        Transaction tx = accountService.deposit(session.getUserId(), number, amount, null);
        ConsoleIO.info("Deposit successful — new balance " + tx.getBalanceAfter());
        printReceipt(tx);
    }

    private void withdraw() {
        String number = ConsoleIO.readNonEmpty("Account number: ");
        requireOwnership(number);
        BigDecimal amount = ConsoleIO.readAmount("Amount to withdraw: ");

        Transaction tx = accountService.withdraw(session.getUserId(), number, amount, null);
        ConsoleIO.info("Withdrawal successful — new balance " + tx.getBalanceAfter());
        printReceipt(tx);
    }

    private void transfer() {
        String from = ConsoleIO.readNonEmpty("From account number: ");
        requireOwnership(from);
        String to = ConsoleIO.readNonEmpty("To account number   : ");
        BigDecimal amount = ConsoleIO.readAmount("Amount              : ");

        String reference = transferService.transfer(session.getUserId(), from, to, amount,
                "Transfer by " + session.getUser().getUsername());
        ConsoleIO.info("Transfer complete — reference " + reference);
    }

    private void miniStatement() {
        String number = ConsoleIO.readNonEmpty("Account number: ");
        requireOwnership(number);
        Account account = accountService.getAccountByNumber(number);
        List<Transaction> transactions = transactionService.getRecentTransactions(account.getAccountId(), 10);
        printStatement(transactions);
    }

    private void rangedStatement() {
        String number = ConsoleIO.readNonEmpty("Account number: ");
        requireOwnership(number);
        Account account = accountService.getAccountByNumber(number);

        LocalDate from = readDate("From date (YYYY-MM-DD): ");
        LocalDate to = readDate("To date   (YYYY-MM-DD): ");

        List<Transaction> transactions = transactionService.getStatement(account.getAccountId(), from, to);
        TransactionService.StatementTotals totals = transactionService.computeTotals(transactions);
        printStatement(transactions);
        ConsoleIO.info(String.format("Entries: %d | Money in: %s | Money out: %s",
                totals.entries(), totals.totalIn(), totals.totalOut()));
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private List<Account> myAccounts() {
        return accountService.getAccountsByCustomer(requireCustomerId());
    }

    private long requireCustomerId() {
        Long customerId = session.getUser().getCustomerId();
        if (customerId == null) throw new IllegalStateException("This login is not linked to a customer profile.");
        return customerId;
    }

    /**
     * Authorization check — a customer session may only touch accounts it owns.
     * Prevents horizontal privilege escalation through guessed account numbers.
     */
    private void requireOwnership(String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        if (account.getCustomerId() != requireCustomerId()) {
            throw new IllegalStateException("You are not authorised to operate on account " + accountNumber + ".");
        }
    }

    private void printStatement(List<Transaction> transactions) {
        ConsoleIO.header("STATEMENT");
        if (transactions.isEmpty()) {
            ConsoleIO.info("(no transactions)");
            return;
        }
        System.out.printf("  %-16s %-12s %10s %12s %-24s%n",
                "DATE", "TYPE", "AMOUNT", "BALANCE", "DESCRIPTION");
        for (Transaction tx : transactions) {
            System.out.printf("  %-16s %-12s %10s %12s %-24s%n",
                    DateUtil.format(tx.getCreatedAt()), tx.getTransactionType(),
                    tx.getAmount(), tx.getBalanceAfter(), shortDescription(tx.getDescription()));
        }
    }

    private void printReceipt(Transaction tx) {
        try {
            Path receipt = ReceiptPrinter.printTransactionReceipt(tx, RECEIPTS_DIR);
            ConsoleIO.info("Receipt saved: " + receipt);
        } catch (Exception e) {
            ConsoleIO.error("Could not save receipt: " + e.getMessage());
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            try {
                return LocalDate.parse(ConsoleIO.readNonEmpty(prompt));
            } catch (Exception e) {
                ConsoleIO.error("Enter a valid date, e.g. 2026-01-31");
            }
        }
    }

    private String shortDescription(String description) {
        if (description == null) return "";
        return description.length() <= 24 ? description : description.substring(0, 21) + "...";
    }

    private int timeoutMinutes() {
        return AppConfig.getInstance().getIntProperty("app.session.timeout.minutes", 30);
    }

    private boolean sessionExpired() {
        if (session.isExpired(timeoutMinutes())) {
            ConsoleIO.error("Session expired due to inactivity.");
            return true;
        }
        return false;
    }

    private void logout() {
        authService.logout(session.getUser());
        ConsoleIO.info("Logged out.");
    }
}
