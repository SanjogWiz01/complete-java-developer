package com.jyotibank.service;

import com.jyotibank.dao.AccountDao;
import com.jyotibank.dao.ReportDao;
import com.jyotibank.model.Account;
import com.jyotibank.model.enums.AccountStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportService {

    private final AccountDao accountDao;
    private final ReportDao reportDao;

    public ReportService(AccountDao accountDao, ReportDao reportDao) {
        this.accountDao = accountDao;
        this.reportDao = reportDao;
    }

    public record AccountPortfolioReport(int totalAccounts, BigDecimal totalBalance, BigDecimal averageBalance) {}

    /** Portfolio summary for an arbitrary set of accounts — computed concurrently. */
    public AccountPortfolioReport summarizeAccounts(List<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return new AccountPortfolioReport(0, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            CompletableFuture<BigDecimal> totalBalanceFuture = CompletableFuture.supplyAsync(
                    () -> accounts.stream()
                            .map(Account::getBalance)
                            .reduce(BigDecimal.ZERO, BigDecimal::add),
                    executor);

            CompletableFuture<Integer> totalCountFuture = CompletableFuture.supplyAsync(accounts::size, executor);

            int totalCount = totalCountFuture.join();
            BigDecimal totalBalance = totalBalanceFuture.join();
            BigDecimal average = totalCount == 0
                    ? BigDecimal.ZERO
                    : totalBalance.divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);

            return new AccountPortfolioReport(totalCount, totalBalance, average);
        } finally {
            executor.shutdown();
        }
    }

    public AccountPortfolioReport portfolioForCustomer(long customerId) {
        return summarizeAccounts(accountDao.findByCustomerId(customerId));
    }

    public List<Account> allAccounts() {
        return accountDao.findAll();
    }

    /** Bank-wide deposit liability (sum of ACTIVE balances). */
    public BigDecimal totalDepositLiability() {
        return reportDao.totalActiveDeposits();
    }

    public List<ReportDao.DormancyCandidate> dormancyCandidates(int days) {
        return reportDao.findDormancyCandidates(days);
    }

    public List<ReportDao.TypeCount> accountCountsByType() {
        return reportDao.countAccountsByType();
    }
}
