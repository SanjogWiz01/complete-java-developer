package com.jyotibank.exception;

public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(String message) { super(message); }
    public AccountNotFoundException(String message, Throwable cause) { super(message, cause); }
}
