package com.example.splitt.bill.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserSplitOutDto extends UserSplitDto {

    private String userName;

    public UserSplitOutDto(Long userId, String userName, Float amountPaid) {
        super(userId, amountPaid);
        this.userName = userName;
    }
}
