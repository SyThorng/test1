package com.kshrd.soccer_date.exception;

import lombok.Data;


public class ForbiddenException extends RuntimeException{
    private String title;

    public ForbiddenException(String title,String message) {
        super(message);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

