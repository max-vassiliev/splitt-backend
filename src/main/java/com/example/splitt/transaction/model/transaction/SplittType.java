package com.example.splitt.transaction.model.transaction;

import com.example.splitt.error.exception.CustomValidationException;

public enum SplittType {

    EQUALLY,
    PARTS,
    SHARES,
    NONE;

    public static SplittType fromString(String string) {
        try {
            return SplittType.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new CustomValidationException("Unknown Splitt Type: " + string);
        }
    }
}
