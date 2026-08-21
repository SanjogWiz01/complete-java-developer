package com.jyotibank.exception;

public class CustomerNotFoundException extends BankingException {
    public CustomerNotFoundException(String message) { super(message); }
    public CustomerNotFoundException(String message, Throwable cause) { super(message, cause); }
}
