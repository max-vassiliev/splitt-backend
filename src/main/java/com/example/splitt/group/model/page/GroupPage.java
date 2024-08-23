package com.example.splitt.group.model.page;

import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.util.balance.model.UserBalance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupPage {

    private List<UserBalance> balances;

    private List<Transaction> transactions;

    public GroupPage(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
