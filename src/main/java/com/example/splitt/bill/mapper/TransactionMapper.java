package com.example.splitt.bill.mapper;

import com.example.splitt.util.balance.dto.UserSplitDto;
import com.example.splitt.bill.model.bill.Bill;
import com.example.splitt.bill.model.transaction.Transaction;
import com.example.splitt.bill.model.transaction.TransactionType;
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
            transaction.setAmount(convertAmount(userSplitDto.getAmount()));
            transaction.setType(transactionType);
            transaction.setGroup(group);
            transaction.setBill(bill);
            transactions.add(transaction);
        }

        return transactions;
    }

    public Transaction toRepayment(User user, Group group, Bill bill, int amount, boolean isRepaymentFrom) {
        Transaction repayment = new Transaction();
        if (isRepaymentFrom) {
            repayment.setType(TransactionType.REPAYMENT_FROM);
        } else {
            repayment.setType(TransactionType.REPAYMENT_TO);
        }
        repayment.setGroup(group);
        repayment.setBill(bill);
        repayment.setUser(user);
        repayment.setAmount(amount);
        return  repayment;
    }

    private int convertAmount(float amountDto) {
        return (int) (amountDto * AMOUNT_CONVERSION_FACTOR);
    }
}
