package com.billykid.library.exception;

public class DBUserNotFoundException extends RuntimeException {
    public DBUserNotFoundException(String message) {
        super(message);
    }
    
}
