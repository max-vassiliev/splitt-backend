package com.example.splitt.error.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DatabaseValidationException extends DataIntegrityViolationException {

    public DatabaseValidationException(String msg) {
        super(msg);
    }

    public DatabaseValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
