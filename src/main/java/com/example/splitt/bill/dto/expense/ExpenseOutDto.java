package com.example.splitt.bill.dto.expense;

import com.example.splitt.bill.dto.bill.BillOutDto;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.dto.UserSplittOutDto;
import com.example.splitt.user.dto.UserOutShortDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpenseOutDto extends BillOutDto {

    private Long id;

    private String title;

    private String note;

    private int amount;

    private String date;

    private UserOutShortDto addedBy;

    private String addedOn;

    private List<UserSplittOutDto> paidBy;

    private List<UserSplittOutDto> debtShares;

    private List<UserBalanceOutDto> groupBalances;

}
