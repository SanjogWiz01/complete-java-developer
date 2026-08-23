package com.jyotibank.service;

import com.jyotibank.dao.TransactionDao;
import com.jyotibank.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {

    private final TransactionDao transactionDao;

    public TransactionService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    /** Mini statement — the most recent {@code limit} ledger rows. */
    public List<Transaction> getRecentTransactions(long accountId, int limit) {
        return transactionDao.findByAccountId(accountId, limit);
    }

    /** Full statement between two dates (inclusive). */
    public List<Transaction> getStatement(long accountId, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date must not be after end date.");
        }
        return transactionDao.findByAccountIdAndDateRange(accountId, from, to);
    }

    public record StatementTotals(int entries, BigDecimal totalIn, BigDecimal totalOut) {}

    /** Signed cash-flow totals for a list of transactions (credits in, debits out). */
    public StatementTotals computeTotals(List<Transaction> transactions) {
        BigDecimal totalIn = BigDecimal.ZERO;
        BigDecimal totalOut = BigDecimal.ZERO;
        for (Transaction tx : transactions) {
            switch (tx.getTransactionType()) {
                case DEPOSIT, INTEREST -> totalIn = totalIn.add(tx.getAmount());
                case WITHDRAWAL, TRANSFER, FEE -> totalOut = totalOut.add(tx.getAmount());
            }
        }
        return new StatementTotals(transactions.size(), totalIn, totalOut);
    }
}
