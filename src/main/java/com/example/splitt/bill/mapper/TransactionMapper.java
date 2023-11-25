package com.example.splitt.bill.mapper;

import com.example.splitt.bill.dto.UserSplitDto;
import com.example.splitt.bill.model.Bill;
import com.example.splitt.bill.model.Transaction;
import com.example.splitt.bill.model.TransactionType;
import com.example.splitt.group.model.Group;
import com.example.splitt.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionMapper {

    private static final int AMOUNT_CONVERSION_FACTOR = 100;

    public List<Transaction> toTransactions(TransactionType transactionType,
                                            List<UserSplitDto> userShares,
                                            Group group,
                                            Bill bill,
                                            Map<Long, User> groupMembers) {
        List<Transaction> transactions = new ArrayList<>();

        for (UserSplitDto userSplitDto : userShares) {
            Transaction transaction = new Transaction();
            transaction.setUser(groupMembers.get(userSplitDto.getUserId()));
            transaction.setAmount(convertAmount(userSplitDto.getSplitAmount()));
            transaction.setType(transactionType);
            transaction.setGroup(group);
            transaction.setBill(bill);
            transactions.add(transaction);
        }

        return transactions;
    }

    private int convertAmount(float amountDto) {
        return (int) amountDto * AMOUNT_CONVERSION_FACTOR;
    }
}
