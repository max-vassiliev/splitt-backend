package com.example.splitt.transaction.dto.expense;

import com.example.splitt.transaction.dto.transaction.TransactionOutShortDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ExpenseOutShortDto extends TransactionOutShortDto {

    private String title;

}
