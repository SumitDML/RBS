package com.dml.project.rbs.exception;

public class InvalidArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public InvalidArgumentException(String message) {
        super(message);
    }
}