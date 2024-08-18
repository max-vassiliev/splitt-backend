package com.example.splitt.util;

import com.example.splitt.error.exception.CustomValidationException;
import com.example.splitt.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;

@Component
@Transactional(readOnly = true)
public class SplittValidator {

    public boolean isEmpty(String str) {
        return str != null && str.trim().isEmpty();
    }

    public <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public Predicate<String> isNotBlankString() {
        return str -> str != null && !str.trim().isEmpty();
    }

    public Predicate<String> isBlankString() {
        return str -> str != null && str.trim().isEmpty();
    }

    public boolean isUserRegistered(User user) {
        return user.getEmail() != null && user.getPassword() != null;
    }

    public void validateUserIsRegistered(User user) {
        if (!isUserRegistered(user)) {
            throw new CustomValidationException("User Not Registered. " +
                    "Only registered users can perform this operation.");
        }
    }

}
