package com.jyotibank.exception;

public class InvalidAccountException extends BankingException {
    public InvalidAccountException(String message) { super(message); }
    public InvalidAccountException(String message, Throwable cause) { super(message, cause); }
}
