package com.example.splitt.bill.dto;

import com.example.splitt.user.dto.UserOutShortDto;
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

    private String note;

    private float amount;

    private String date;

    private UserOutShortDto addedBy;

    private String addedOn;

    List<UserSplitOutDto> paidBy;

    List<UserSplitOutDto> debtShares;

    List<GroupBalanceOutDto> groupBalance;

}
