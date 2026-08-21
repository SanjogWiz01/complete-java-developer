package com.jyotibank.exception;

public class AuthenticationException extends BankingException {
    public AuthenticationException(String message) { super(message); }
    public AuthenticationException(String message, Throwable cause) { super(message, cause); }
}
