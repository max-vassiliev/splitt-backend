package com.example.splitt.util.validation.annotations;

import com.example.splitt.util.AppConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AvatarValidator implements ConstraintValidator<ValidAvatar, String> {

    @Override
    public boolean isValid(String avatar, ConstraintValidatorContext constraintValidatorContext) {
        if (avatar == null || avatar.equals(AppConstants.DEFAULT_AVATAR)) {
            return true;
        }

        return avatar.matches("^(?!\\s*$).+\\.(jpg|png)$");
    }
}
