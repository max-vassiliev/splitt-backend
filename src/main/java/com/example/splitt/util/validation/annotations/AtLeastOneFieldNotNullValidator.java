package com.example.splitt.util.validation.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AtLeastOneFieldNotNullValidator implements ConstraintValidator<AtLeastOneFieldNotNull, Object> {

    private String[] fields;

    @Override
    public void initialize(AtLeastOneFieldNotNull constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext constraintValidatorContext) {
        try {
            for (String fieldName : fields) {
                Field field = dto.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(dto);

                if (value != null) {
                    return true;
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException("Error accessing fields during validation", exception);
        }

        return false;
    }
}
