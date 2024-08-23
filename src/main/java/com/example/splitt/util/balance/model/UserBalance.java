package com.example.splitt.util.balance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserBalance {

    private Long userId;

    private int amount;

    public UserBalance(Long userId,  Long amount) {
        this.userId = userId;
        this.amount = amount != null ? amount.intValue() : 0;
    }
}
