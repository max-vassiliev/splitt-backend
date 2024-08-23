package com.example.splitt.group.dto.page;

import com.example.splitt.transaction.dto.transaction.TransactionOutShortDto;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
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
public class GroupPageDto {

    private List<UserBalanceOutDto> balances;

    private List<TransactionOutShortDto> transactions;

}
