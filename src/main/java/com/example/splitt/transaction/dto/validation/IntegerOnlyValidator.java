package com.example.splitt.transaction.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IntegerOnlyValidator implements ConstraintValidator<IntegerOnly, Number> {

    @Override
    public void initialize(IntegerOnly constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return value.doubleValue() == value.intValue();
    }
}
