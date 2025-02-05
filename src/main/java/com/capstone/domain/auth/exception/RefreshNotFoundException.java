package com.capstone.domain.auth.exception;

public class RefreshNotFoundException extends RuntimeException {
    public RefreshNotFoundException(String message) {
        super(message);
    }
}
