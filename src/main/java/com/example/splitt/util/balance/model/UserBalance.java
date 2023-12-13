package com.example.splitt.util.balance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserBalance {

    private Long userId;

    private String userName;

    private int amount;

    public UserBalance(Long userId, String userName, Long amount) {
        this.userId = userId;
        this.userName = userName;
        this.amount = amount != null ? amount.intValue() : 0;
    }
}
