package com.example.splitt.bill.repository;

import com.example.splitt.bill.model.transaction.Transaction;
import com.example.splitt.bill.model.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select new com.example.splitt.bill.model.UserBalance(t.user.id, " +
            "sum(case when t.type = 'EXPENSE' then t.amount " +
            "when t.type = 'REPAYMENT' then t.amount " +
            "when t.type = 'DEBT' then -t.amount " +
            "else 0 " +
            "end)) " +
            "from Transaction t " +
            "where t.group.id = :groupId " +
            "group by t.user.id " +
            "order by t.user.id")
    List<UserBalance> getUserBalancesInGroup(@Param("groupId") Long groupId);

}
