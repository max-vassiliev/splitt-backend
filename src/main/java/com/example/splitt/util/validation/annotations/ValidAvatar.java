package com.example.splitt.util.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AvatarValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAvatar {

    String message() default "Invalid avatar format. It should be a non-empty string ending with .jpg or .png.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
