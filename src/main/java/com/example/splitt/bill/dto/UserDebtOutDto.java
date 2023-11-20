package com.example.splitt.bill.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDebtOutDto extends UserDebtDto {

    private String userName;

    public UserDebtOutDto(Long userId, String userName, float amountOwed) {
        super(userId, amountOwed);
        this.userName = userName;
    }
}
