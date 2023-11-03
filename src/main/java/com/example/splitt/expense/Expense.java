package com.example.splitt.expense;

import com.example.splitt.bill.model.Transaction;
import com.example.splitt.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Expense {

    private Long id;
    private User addedBy;
    private User paidBy;
    private String title;
    private int amount;
    private String note;
    private LocalDateTime date;
    private Set<Transaction> transactions;

}
