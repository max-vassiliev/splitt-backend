package com.example.splitt.util.balance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserBalanceOutDto {

    private Long userId;

    private String userName;

    private float balance;

    private List<UserSplitOutDto> details;

}
