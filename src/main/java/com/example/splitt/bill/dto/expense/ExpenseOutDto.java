package com.example.splitt.bill.dto.expense;

import com.example.splitt.bill.dto.GroupBalanceOutDto;
import com.example.splitt.bill.dto.bill.BillOutDto;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.dto.UserSplitOutDto;
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

    private float amount;

    private String date;

    private UserOutShortDto addedBy;

    private String addedOn;

    private List<UserSplitOutDto> paidBy;

    private List<UserSplitOutDto> debtShares;

    private List<UserBalanceOutDto> groupBalances;

}
