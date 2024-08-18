package com.example.splitt.bill.repository;

import com.example.splitt.bill.model.transaction.Transaction;
import com.example.splitt.util.balance.model.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    // TODO обновить запрос с REPAYMENT:
    // для внесшего платеж это +amount
    // для получателя это -amount

    @Query("select new com.example.splitt.util.balance.model.UserBalance(t.user.id," +
            "t.user.name, " +
            "sum(case when t.type = 'PAYMENT' then t.amount " +
            "when t.type = 'REPAYMENT_FROM' then t.amount " +
            "when t.type = 'REPAYMENT_TO' then -t.amount  " +
            "when t.type = 'DEBT' then -t.amount " +
            "else 0 " +
            "end)) " +
            "from Transaction t " +
            "where t.group.id = :groupId " +
            "group by t.user.id, t.user.name " +
            "order by t.user.id")
    List<UserBalance> getUserBalancesInGroup(@Param("groupId") Long groupId);

    List<Transaction> findAllByBill_IdIn(List<Long> billIds);
}
