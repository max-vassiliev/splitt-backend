package com.example.splitt.page.model;

import com.example.splitt.group.model.Group;
import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupPageFull extends GroupPage {

    private User user;

    private Group group;

    public GroupPageFull(User user, Group group, List<Transaction> transactions) {
        super(transactions);
        this.user = user;
        this.group = group;
    }
}
