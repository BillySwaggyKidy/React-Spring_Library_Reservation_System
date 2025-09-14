package com.billykid.library.exception;

public class BookNotAvailableForReservation extends RuntimeException {
    public BookNotAvailableForReservation(String message) {
        super(message);
    }
}
