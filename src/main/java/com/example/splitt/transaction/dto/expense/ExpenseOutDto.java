package com.example.splitt.transaction.dto.expense;

import com.example.splitt.transaction.dto.transaction.TransactionOutDto;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.dto.UserSplittOutDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpenseOutDto extends TransactionOutDto {

    private Long id;

    private String title;

    private int amount;

    private String emoji;

    private String date;

    private String splittType;

    private Long addedByUserId;

    private String addedOn;

    private String note;

    private List<UserSplittOutDto> paidBy;

    private List<UserSplittOutDto> splitts;

    // TODO удалить
    private List<UserBalanceOutDto> groupBalances;

}
