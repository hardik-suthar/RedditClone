package com.project.redditClone.exception;

public class TooLongLengthException extends RuntimeException{
    public TooLongLengthException(String message) {
        super(message);
    }
}
