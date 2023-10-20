package com.kshrd.soccer_date.exception;

public class AttendanceException extends RuntimeException{
    private String title;
    public AttendanceException(String title,String message) {
        super(message);
        this.title=title;
    }

    public String getTitle() {
        return title;
    }
}
