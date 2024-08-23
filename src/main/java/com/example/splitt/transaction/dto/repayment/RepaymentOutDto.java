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

//    private UserOutShortDto payer;
    private Long payerId;

//    private UserOutShortDto recipient;
    private Long recipientId;

//    private UserOutShortDto addedBy;
    private Long addedByUserId;

    // TODO удалить
    private List<UserBalanceOutDto> groupBalances;

}
