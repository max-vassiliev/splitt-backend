package com.example.splitt.transaction.dto.repayment;

import com.example.splitt.transaction.dto.transaction.TransactionOutDto;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RepaymentOutDto extends TransactionOutDto {

    private Long id;

    private String note;

    private int amount;

    private String emoji;

    private String date;

    private String addedOn;

    private Long payerId;

    private Long recipientId;

    private Long addedByUserId;

    private List<UserBalanceOutDto> groupBalances;

}
