package com.jyotibank.dao;

import com.jyotibank.model.Transaction;
import com.jyotibank.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionDao {
    long create(Transaction transaction);
    List<Transaction> findByAccountId(long accountId, int limit);
    List<Transaction> findByAccountIdAndDateRange(long accountId, LocalDate from, LocalDate to);
    BigDecimal sumAmountByTypeOnDate(long accountId, TransactionType type, LocalDate date);
}
