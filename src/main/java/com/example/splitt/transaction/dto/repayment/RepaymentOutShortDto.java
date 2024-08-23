package com.example.splitt.transaction.dto.repayment;

import com.example.splitt.transaction.dto.transaction.TransactionOutShortDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RepaymentOutShortDto extends TransactionOutShortDto {

    private Long payerId;

    private Long recipientId;

}
