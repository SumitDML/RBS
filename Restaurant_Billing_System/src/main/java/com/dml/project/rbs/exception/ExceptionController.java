package com.dml.project.rbs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<Object> exception1(ItemNotFoundException exception) {
        return new ResponseEntity<>("Items not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidOtpException.class)
    public ResponseEntity<Object> exception2(InvalidOtpException exception) {
        return new ResponseEntity<>("Invalid OTP Please Try Again", HttpStatus.NOT_FOUND);
    }
}