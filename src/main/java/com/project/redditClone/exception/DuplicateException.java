package com.project.redditClone.exception;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String message) {
        super(message);
    }
}
