package com.example.splitt.bill.dto.repayment;

import com.example.splitt.bill.dto.GroupBalanceOutDto;
import com.example.splitt.bill.dto.bill.BillOutDto;
import com.example.splitt.user.dto.UserOutShortDto;
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
public class RepaymentOutDto extends BillOutDto {

    private Long id;

    private String title;

    private String note;

    private float amount;

    private String date;

    private String addedOn;

    private UserOutShortDto payer;

    private UserOutShortDto recipient;

    private UserOutShortDto addedBy;

    List<UserBalanceOutDto> groupBalances;

}
