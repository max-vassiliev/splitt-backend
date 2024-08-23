package com.example.splitt.transaction.mapper;

import com.example.splitt.transaction.model.entry.Entry;
import com.example.splitt.util.balance.dto.UserSplittDto;
import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.transaction.model.entry.EntryType;
import com.example.splitt.group.model.Group;
import com.example.splitt.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EntryMapper {

    public List<Entry> toEntries(EntryType entryType,
                                      List<UserSplittDto> userShares,
                                      Group group,
                                      Transaction transaction) {
        List<Entry> entries = new ArrayList<>();

        for (UserSplittDto userSplitDto : userShares) {
            Entry entry = new Entry();
            entry.setUserId(userSplitDto.getUserId());
            entry.setAmount(userSplitDto.getAmount());
            entry.setType(entryType);
            entry.setGroup(group);
            entry.setTransaction(transaction);
            entries.add(entry);
        }

        return entries;
    }

    public Entry toRepayment(User user, Group group, Transaction transaction, int amount, boolean isRepaymentFrom) {
        Entry repayment = new Entry();
        if (isRepaymentFrom) {
            repayment.setType(EntryType.REPAYMENT_FROM);
        } else {
            repayment.setType(EntryType.REPAYMENT_TO);
        }
        repayment.setGroup(group);
        repayment.setTransaction(transaction);
        repayment.setUserId(user.getId());
        repayment.setAmount(amount);
        return repayment;
    }
}
