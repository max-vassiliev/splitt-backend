package com.example.splitt.transaction.dto.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TransactionOutShortDto {

    private Long id;

    private String type;

    private int amount;

    private Integer currentUserBalance;

    private String date;

    private String emoji;

}
