package com.kshrd.soccer_date.exception;
public class LikeException extends RuntimeException{
    String title;
    public LikeException(String message,String title) {
        super(message);
        this.title=title;
    }
}
