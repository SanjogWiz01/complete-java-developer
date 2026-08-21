package com.jyotibank.service;

import com.jyotibank.dao.AccountDao;
import com.jyotibank.dao.FixedDepositDao;
import com.jyotibank.exception.AccountNotFoundException;
import com.jyotibank.exception.InvalidAmountException;
import com.jyotibank.model.Account;
import com.jyotibank.model.FixedDeposit;
import com.jyotibank.model.enums.FDStatus;
import com.jyotibank.util.InputValidator;

import java.math.BigDecimal;

public class FixedDepositService {

    private final AccountDao accountDao;
    private final FixedDepositDao fixedDepositDao;

    public FixedDepositService(AccountDao accountDao, FixedDepositDao fixedDepositDao) {
        this.accountDao = accountDao;
        this.fixedDepositDao = fixedDepositDao;
    }

    public FixedDeposit create(long linkedAccountId, BigDecimal amount, BigDecimal annualRate, int tenureMonths) {
        if (!InputValidator.isValidAmount(amount)) {
            throw new InvalidAmountException("FD amount must be greater than zero.");
        }
        if (tenureMonths <= 0) {
            throw new InvalidAmountException("FD tenure must be greater than zero.");
        }
        Account linked = accountDao.findById(linkedAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Linked account not found: " + linkedAccountId));

        FixedDeposit fixedDeposit = new FixedDeposit(linked.getAccountId(), amount, annualRate, tenureMonths);
        fixedDeposit.setStatus(FDStatus.ACTIVE);
        long fdId = fixedDepositDao.create(fixedDeposit);
        fixedDeposit.setFdId(fdId);
        return fixedDeposit;
    }
}
