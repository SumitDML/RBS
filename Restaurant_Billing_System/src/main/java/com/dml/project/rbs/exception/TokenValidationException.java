package com.dml.project.rbs.exception;

import io.jsonwebtoken.ExpiredJwtException;

public class TokenValidationException extends RuntimeException{
    public TokenValidationException(String message) {
        super(message);
    }
}
