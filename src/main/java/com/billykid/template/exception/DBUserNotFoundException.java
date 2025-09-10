package com.billykid.template.exception;

public class DBUserNotFoundException extends RuntimeException {
    public DBUserNotFoundException(String message) {
        super(message);
    }
    
}
