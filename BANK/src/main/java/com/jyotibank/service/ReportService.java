package com.jyotibank.service;

import com.jyotibank.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportService {

    public record AccountPortfolioReport(int totalAccounts, BigDecimal totalBalance, BigDecimal averageBalance) {}

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
                    : totalBalance.divide(BigDecimal.valueOf(totalCount), 2, java.math.RoundingMode.HALF_UP);

            return new AccountPortfolioReport(totalCount, totalBalance, average);
        } finally {
            executor.shutdown();
        }
    }
}
