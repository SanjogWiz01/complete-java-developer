package com.cabbooking.exception;

public class InvalidBookingStateException extends CabBookingException {
    public InvalidBookingStateException(String message) {
        super(message);
    }
}
