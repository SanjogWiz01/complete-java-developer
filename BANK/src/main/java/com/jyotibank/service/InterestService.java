package com.jyotibank.service;

import com.jyotibank.config.AppConfig;
import com.jyotibank.dao.AccountDao;
import com.jyotibank.dao.TransactionDao;
import com.jyotibank.model.Account;
import com.jyotibank.model.Transaction;
import com.jyotibank.model.enums.AccountStatus;
import com.jyotibank.model.enums.AccountType;
import com.jyotibank.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * InterestService — periodic interest posting job.
 *
 * <p>Demonstrates polymorphism in action: every active account knows how to
 * compute its own monthly interest via {@link Account#calculateMonthlyInterest()}.
 * The service simply iterates and credits — it never needs to know whether an
 * account is a SavingsAccount or a CurrentAccount.
 *
 * <p>In production this would be a scheduled job (e.g., cron / Quartz);
 * here an admin triggers it from the console menu.
 */
public class InterestService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;
    private final AuditService auditService;

    public InterestService(AccountDao accountDao, TransactionDao transactionDao, AuditService auditService) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
        this.auditService = auditService;
    }

    /** Posts one month of interest to every ACTIVE savings/current account. Returns summary. */
    public record InterestRun(int accountsCredited, BigDecimal totalInterestPosted) {}

    public InterestRun applyMonthlyInterest(long actorUserId) {
        List<Account> accounts = accountDao.findAll();
        int credited = 0;
        BigDecimal total = BigDecimal.ZERO;

        for (Account account : accounts) {
            if (account.getStatus() != AccountStatus.ACTIVE) continue;
            if (account.getAccountType() == AccountType.FIXED_DEPOSIT) continue; // FD pays at maturity

            BigDecimal interest = account.calculateMonthlyInterest();
            if (interest == null || interest.compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal before = account.getBalance();
            BigDecimal after = before.add(interest).setScale(2, java.math.RoundingMode.HALF_UP);

            accountDao.updateBalance(account.getAccountId(), after);
            transactionDao.create(new Transaction(
                    reference(),
                    account.getAccountId(),
                    TransactionType.INTEREST,
                    interest,
                    before,
                    after,
                    "Monthly interest @ " + ratePercent(account) + "% p.a."
            ));
            credited++;
            total = total.add(interest);
        }

        auditService.record(actorUserId, "INTEREST_APPLIED", "ACCOUNT", null,
                "Posted interest to " + credited + " accounts, total " + total);
        return new InterestRun(credited, total);
    }

    private String ratePercent(Account account) {
        BigDecimal rate = account.getInterestRate();
        return rate == null ? AppConfig.getInstance().getProperty("interest.savings.annual", "0") : rate.toString();
    }

    private String reference() {
        return "INT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
