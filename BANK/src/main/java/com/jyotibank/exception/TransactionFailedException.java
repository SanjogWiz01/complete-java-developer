package com.jyotibank.exception;

public class TransactionFailedException extends BankingException {
    public TransactionFailedException(String message) { super(message); }
    public TransactionFailedException(String message, Throwable cause) { super(message, cause); }
}
