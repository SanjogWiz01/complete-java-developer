package com.jyotibank.presentation;

import com.jyotibank.model.Account;
import com.jyotibank.model.FixedDeposit;
import com.jyotibank.service.AccountService;
import com.jyotibank.service.FixedDepositService;
import com.jyotibank.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class FixedDepositUI {

    private final SessionContext session;
    private final FixedDepositService fixedDepositService;
    private final AccountService accountService;

    public FixedDepositUI(SessionContext session,
                          FixedDepositService fixedDepositService,
                          AccountService accountService) {
        this.session = session;
        this.fixedDepositService = fixedDepositService;
        this.accountService = accountService;
    }

    public void show() {
        boolean running = true;
        while (running) {
            ConsoleIO.header("FIXED DEPOSITS");
            System.out.println("1. Open a fixed deposit");
            System.out.println("2. View my fixed deposits");
            System.out.println("3. Break FD early (penalty applies)");
            System.out.println("4. Settle matured FD");
            System.out.println("0. Back");
            String option = ConsoleIO.readLine("Choose an option: ");

            try {
                switch (option) {
                    case "1" -> openFd();
                    case "2" -> listFds();
                    case "3" -> breakFd();
                    case "4" -> settleFd();
                    case "0" -> running = false;
                    default -> ConsoleIO.error("Invalid option.");
                }
            } catch (Exception e) {
                ConsoleIO.error(e.getMessage());
            }
        }
    }

    private void openFd() {
        List<Account> accounts = myAccounts();
        AdminUI.printAccounts("YOUR ACCOUNTS", accounts);
        if (accounts.isEmpty()) return;

        String fromNumber = ConsoleIO.readNonEmpty("Debit which account number: ");
        Account from = myAccounts().stream()
                .filter(a -> a.getAccountNumber().equals(fromNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "You are not authorised to operate on account " + fromNumber + "."));
        BigDecimal amount = ConsoleIO.readAmount("FD principal amount        : ");
        int months = ConsoleIO.readInt("Tenure (months)            : ", 1, 120);

        BigDecimal defaultRate = new BigDecimal("0.0650");
        BigDecimal rate = readRate(defaultRate);

        FixedDeposit fd = fixedDepositService.open(session.getUserId(),
                from.getAccountId(), amount, rate, months);

        ConsoleIO.info("Fixed deposit #" + fd.getFdId() + " opened.");
        printFd(fd);
    }

    private void listFds() {
        ConsoleIO.header("MY FIXED DEPOSITS");
        List<FixedDeposit> all = new java.util.ArrayList<>();
        for (Account account : myAccounts()) {
            all.addAll(fixedDepositService.getByLinkedAccount(account.getAccountId()));
        }
        if (all.isEmpty()) {
            ConsoleIO.info("(none)");
            return;
        }
        for (FixedDeposit fd : all) printFd(fd);
    }

    private void breakFd() {
        long fdId = ConsoleIO.readLong("FD id to break: ");
        FixedDeposit fd = fixedDepositService.getFd(fdId);
        requireOwnershipOf(fd);

        FixedDepositAccountSummary preview = summarize(fd);
        BigDecimal penaltyRate = new BigDecimal("0.02"); // matches config default; exact math done in service
        BigDecimal payout = fd.calculateEarlyWithdrawalAmount(penaltyRate).setScale(2, RoundingMode.HALF_UP);

        ConsoleIO.info("Principal: " + preview.principal() + " | Estimated payout after penalty: " + payout);
        if (!ConsoleIO.confirm("Break this fixed deposit now?")) return;

        FixedDepositService.FdBreakResult result = fixedDepositService.breakEarly(session.getUserId(), fdId);
        ConsoleIO.info("FD broken. Principal " + result.principal()
                + ", penalty " + result.penalty() + ", credited " + result.payout());
    }

    private void settleFd() {
        long fdId = ConsoleIO.readLong("FD id to settle: ");
        FixedDeposit fd = fixedDepositService.getFd(fdId);
        requireOwnershipOf(fd);
        ConsoleIO.info("Maturity amount: " + fd.getMaturityAmount()
                + " (matures " + DateUtil.format(fd.getMaturityDate()) + ")");
        if (!ConsoleIO.confirm("Credit the maturity amount to your linked account?")) return;

        FixedDepositService.FdBreakResult result = fixedDepositService.settleAtMaturity(session.getUserId(), fdId);
        ConsoleIO.info("Settled. Credited " + result.payout() + " (interest earned "
                + result.payout().subtract(result.principal()) + ")");
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private void printFd(FixedDeposit fd) {
        System.out.printf("  #%d | P=%s @ %s for %dm | maturity %s -> %s | status=%s%n",
                fd.getFdId(), fd.getPrincipalAmount(), fd.getInterestRate(), fd.getTenureMonths(),
                DateUtil.format(fd.getMaturityDate()), fd.getMaturityAmount(), fd.getStatus());
    }

    private FixedDepositAccountSummary summarize(FixedDeposit fd) {
        return new FixedDepositAccountSummary(fd.getPrincipalAmount(), fd.getMaturityAmount());
    }

    private record FixedDepositAccountSummary(BigDecimal principal, BigDecimal maturityAmount) {}

    private List<Account> myAccounts() {
        Long customerId = session.getUser().getCustomerId();
        if (customerId == null) throw new IllegalStateException("This login has no customer profile.");
        return accountService.getAccountsByCustomer(customerId);
    }

    private void requireOwnershipOf(FixedDeposit fd) {
        Long customerId = session.getUser().getCustomerId();
        Account linked = accountService.getAccountByNumber(
                myAccounts().stream()
                        .filter(a -> a.getAccountId() == fd.getLinkedAccountId())
                        .findFirst()
                        .map(Account::getAccountNumber)
                        .orElseThrow(() -> new IllegalStateException(
                                "You are not authorised to operate on FD #" + fd.getFdId() + ".")));
        if (linked == null || linked.getCustomerId() != customerId) {
            throw new IllegalStateException("You are not authorised to operate on FD #" + fd.getFdId() + ".");
        }
    }

    private BigDecimal readRate(BigDecimal fallback) {
        String input = AdminUI.readOptional("Annual rate [default " + fallback + ", e.g. 0.07]: ");
        if (input.isBlank()) return fallback;
        try {
            BigDecimal rate = new BigDecimal(input);
            if (rate.compareTo(BigDecimal.ZERO) <= 0 || rate.compareTo(new BigDecimal("1")) >= 0) {
                throw new NumberFormatException();
            }
            return rate.setScale(4, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            ConsoleIO.error("Invalid rate — using default.");
            return fallback;
        }
    }
}
