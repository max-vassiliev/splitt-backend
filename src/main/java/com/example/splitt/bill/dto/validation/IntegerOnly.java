package com.example.splitt.bill.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IntegerOnlyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerOnly {

    String message() default "The amount must be a whole number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
