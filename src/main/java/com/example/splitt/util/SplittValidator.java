package com.example.splitt.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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


}
