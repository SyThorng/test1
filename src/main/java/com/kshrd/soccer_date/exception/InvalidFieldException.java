package com.kshrd.soccer_date.exception;

public class InvalidFieldException extends RuntimeException {
    String title;

    public InvalidFieldException(String title,String message) {
        super(message);
        this.title = title;
    }
}
