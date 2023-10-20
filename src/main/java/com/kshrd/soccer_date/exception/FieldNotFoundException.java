package com.kshrd.soccer_date.exception;

public class FieldNotFoundException extends RuntimeException {
    String title;
    public FieldNotFoundException( String title,String message) {
        super(message);
        this.title = title;
    }
}
