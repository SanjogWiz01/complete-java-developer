package com.jyotibank.exception;

/**
 * BankingException — root of the custom exception hierarchy.
 *
 * Extends RuntimeException (unchecked) so callers are not forced to declare
 * "throws BankingException" on every method. In a banking context, most
 * business-rule violations bubble up to the UI layer which catches and displays them.
 *
 * The cause chain is preserved via super(message, cause) so stack traces
 * remain complete for logging and debugging.
 */
public class BankingException extends RuntimeException {

    public BankingException(String message) {
        super(message);
    }

    public BankingException(String message, Throwable cause) {
        super(message, cause);
    }
}
