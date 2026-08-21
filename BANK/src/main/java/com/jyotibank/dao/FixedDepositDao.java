package com.jyotibank.dao;

import com.jyotibank.model.FixedDeposit;

import java.util.List;
import java.util.Optional;

public interface FixedDepositDao {
    long create(FixedDeposit fixedDeposit);
    Optional<FixedDeposit> findById(long fixedDepositId);
    List<FixedDeposit> findByFdAccountId(long fdAccountId);
    void update(FixedDeposit fixedDeposit);
}
