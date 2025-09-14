package com.billykid.library.exception;

public class DBUserUserAlreadyExist extends RuntimeException {
    public DBUserUserAlreadyExist(String message) {
        super(message);
    }
}
