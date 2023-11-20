package com.example.splitt.bill.model;

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
    private int amount;

    public UserBalance(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount != null ? amount.intValue() : 0;
    }
}
