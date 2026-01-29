package com.hotel.common.exception;

public class BookingConflictException extends Exception {
    public BookingConflictException(String message) {
        super(message);
    }
}
