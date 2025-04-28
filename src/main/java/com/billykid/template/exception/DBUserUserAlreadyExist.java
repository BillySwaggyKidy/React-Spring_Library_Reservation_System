package com.billykid.template.exception;

public class DBUserUserAlreadyExist extends RuntimeException {
    public DBUserUserAlreadyExist(String message) {
        super(message);
    }
}
