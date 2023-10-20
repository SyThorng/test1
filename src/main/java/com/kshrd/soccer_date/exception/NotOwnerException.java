package com.kshrd.soccer_date.exception;

public class NotOwnerException extends RuntimeException{
    private String title;
    public NotOwnerException(String message,String title) {
        super(message);
        this.title=title;
    }

    public String getTitle() {
        return title;
    }
}
