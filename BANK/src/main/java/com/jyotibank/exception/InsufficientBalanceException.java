package com.jyotibank.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends BankingException {

    private final BigDecimal available;
    private final BigDecimal required;

    public InsufficientBalanceException(String message) {
        super(message);
        this.available = null;
        this.required  = null;
    }

    public InsufficientBalanceException(BigDecimal available, BigDecimal required) {
        super(String.format("Insufficient balance. Available: NPR %s, Required: NPR %s",
                available, required));
        this.available = available;
        this.required  = required;
    }

    public BigDecimal getAvailable() { return available; }
    public BigDecimal getRequired()  { return required;  }
}
