package com.example.splitt.transaction.repository;

import com.example.splitt.transaction.model.entry.Entry;
import com.example.splitt.util.balance.model.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {


    // TODO обновить запрос с REPAYMENT:
    // для внесшего платеж это +amount
    // для получателя это -amount

    @Query("select new com.example.splitt.util.balance.model.UserBalance(e.userId," +
            "sum(case when e.type = 'PAYMENT' then e.amount " +
            "when e.type = 'REPAYMENT_FROM' then e.amount " +
            "when e.type = 'REPAYMENT_TO' then -e.amount  " +
            "when e.type = 'DEBT' then -e.amount " +
            "else 0 " +
            "end)) " +
            "from Entry e " +
            "where e.group.id = :groupId " +
            "group by e.userId " +
            "order by e.userId")
    List<UserBalance> getUserBalancesInGroup(@Param("groupId") Long groupId);

    List<Entry> findAllByTransaction_IdIn(List<Long> transactionIds);

}
