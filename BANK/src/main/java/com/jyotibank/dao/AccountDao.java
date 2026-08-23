package com.jyotibank.dao;

import com.jyotibank.model.Account;
import com.jyotibank.model.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountDao {
    long create(Account account);
    Optional<Account> findById(long accountId);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(long customerId);
    List<Account> findAll();
    void updateBalance(long accountId, BigDecimal balance);
    void updateStatus(long accountId, AccountStatus accountStatus);
}
