package com.billykid.template.exception;

public class BookNotAvailableForReservation extends RuntimeException {
    public BookNotAvailableForReservation(String message) {
        super(message);
    }
}
