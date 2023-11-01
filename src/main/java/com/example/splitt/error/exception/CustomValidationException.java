package com.example.splitt.error.exception;

import lombok.Getter;

@Getter
public class CustomValidationException extends RuntimeException {

    private final String reason;

    public CustomValidationException(String message) {
        super(message);
        this.reason = "Validation Error.";
    }
}
