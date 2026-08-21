package com.jyotibank.service;

import com.jyotibank.dao.TransactionDao;
import com.jyotibank.model.Transaction;

import java.util.List;

public class TransactionService {

    private final TransactionDao transactionDao;

    public TransactionService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public List<Transaction> getRecentTransactions(long accountId, int limit) {
        return transactionDao.findByAccountId(accountId, limit);
    }
}
