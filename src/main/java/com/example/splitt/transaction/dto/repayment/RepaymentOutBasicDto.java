package com.example.splitt.transaction.dto.repayment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RepaymentOutBasicDto {

    private Long id;

    private int amount;

    private Long payerId;

    private Long recipientId;

    private Long addedBy;

    private String emoji;

    private String date;

    private String note;

}
