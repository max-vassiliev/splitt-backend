package com.example.splitt.bill.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpenseBalanceOutDto {

    private Long id;

    private String title;

    private float amount;

    List<UserSplitOutDto> paidBy;

    List<UserDebtOutDto> shares;

    List<GroupBalanceOutDto> groupBalance;

}
