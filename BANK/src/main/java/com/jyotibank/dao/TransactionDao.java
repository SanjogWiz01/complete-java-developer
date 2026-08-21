package com.jyotibank.dao;

import com.jyotibank.model.Transaction;

import java.util.List;

public interface TransactionDao {
    long create(Transaction transaction);
    List<Transaction> findByAccountId(long accountId, int limit);
}
