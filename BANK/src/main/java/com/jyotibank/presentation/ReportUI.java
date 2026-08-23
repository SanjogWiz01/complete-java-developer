package com.jyotibank.presentation;

import com.jyotibank.dao.ReportDao;
import com.jyotibank.model.Account;
import com.jyotibank.service.FixedDepositService;
import com.jyotibank.service.ReportService;
import com.jyotibank.util.DateUtil;

import java.util.List;

public class ReportUI {

    private final SessionContext session;
    private final ReportService reportService;
    private final FixedDepositService fixedDepositService;

    public ReportUI(SessionContext session,
                    ReportService reportService,
                    FixedDepositService fixedDepositService) {
        this.session = session;
        this.reportService = reportService;
        this.fixedDepositService = fixedDepositService;
    }

    public void show() {
        boolean running = true;
        while (running) {
            ConsoleIO.header("REPORTS");
            System.out.println("1. My portfolio summary");
            System.out.println("2. Bank deposit liability (admin)");
            System.out.println("3. Accounts by type (admin)");
            System.out.println("4. Dormancy candidates — 365 days (admin)");
            System.out.println("0. Back");
            String option = ConsoleIO.readLine("Choose an option: ");

            try {
                switch (option) {
                    case "1" -> portfolioSummary();
                    case "2" -> requireAdmin(this::depositLiability);
                    case "3" -> requireAdmin(this::accountsByType);
                    case "4" -> requireAdmin(() -> dormancy(365));
                    case "0" -> running = false;
                    default -> ConsoleIO.error("Invalid option.");
                }
            } catch (Exception e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private void portfolioSummary() {
        List<Account> accounts = session.isAdmin()
                ? reportService.allAccounts()
                : myAccounts();

        ReportService.AccountPortfolioReport report = reportService.summarizeAccounts(accounts);
        ConsoleIO.header("PORTFOLIO SUMMARY");
        ConsoleIO.info("Accounts      : " + report.totalAccounts());
        ConsoleIO.info("Total balance : " + report.totalBalance());
        ConsoleIO.info("Average       : " + report.averageBalance());

        int activeFdCount = countActiveFds();
        ConsoleIO.info("Active FDs    : " + activeFdCount);
    }

    private void depositLiability() {
        ConsoleIO.header("DEPOSIT LIABILITY");
        ConsoleIO.info("Total held across ACTIVE accounts: "
                + reportService.totalDepositLiability());
    }

    private void accountsByType() {
        ConsoleIO.header("ACCOUNTS BY TYPE");
        List<ReportDao.TypeCount> counts = reportService.accountCountsByType();
        if (counts.isEmpty()) {
            ConsoleIO.info("(no accounts yet)");
            return;
        }
        for (ReportDao.TypeCount tc : counts) {
            System.out.printf("  %-28s %6d%n", tc.accountType(), tc.count());
        }
    }

    private void dormancy(int days) {
        ConsoleIO.header("DORMANCY CANDIDATES (>" + days + " days without activity)");
        List<ReportDao.DormancyCandidate> candidates = reportService.dormancyCandidates(days);
        if (candidates.isEmpty()) {
            ConsoleIO.info("(none — all accounts show recent activity)");
            return;
        }
        System.out.printf("  %-20s %-16s %12s %-20s%n", "NUMBER", "TYPE", "BALANCE", "LAST ACTIVITY");
        for (ReportDao.DormancyCandidate c : candidates) {
            System.out.printf("  %-20s %-16s %12s %-20s%n",
                    c.accountNumber(), c.accountType(), c.balance(),
                    c.lastActivity() == null ? "(never)" : DateUtil.format(c.lastActivity()));
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private interface AdminAction { void run(); }

    private void requireAdmin(AdminAction action) {
        if (!session.isAdmin()) {
            ConsoleIO.error("Admin privileges required.");
            return;
        }
        action.run();
    }

    private List<Account> myAccounts() {
        return session.isAdmin()
                ? reportService.allAccounts()
                : new java.util.ArrayList<>();
    }

    private int countActiveFds() {
        try {
            return (int) fixedDepositsVisibleToMe().stream()
                    .filter(fd -> fd.getStatus() == com.jyotibank.model.enums.FDStatus.ACTIVE)
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }

    private List<com.jyotibank.model.FixedDeposit> fixedDepositsVisibleToMe() {
        Long customerId = session.getUser().getCustomerId();
        if (customerId == null) return List.of();
        // FDs are linked to accounts; enumerate via account service through report data
        List<com.jyotibank.model.FixedDeposit> fds = new java.util.ArrayList<>();
        for (Account account : reportService.allAccounts()) {
            if (account.getCustomerId() != customerId && !session.isAdmin()) continue;
            fds.addAll(fixedDepositService.getByLinkedAccount(account.getAccountId()));
        }
        return fds;
    }
}
