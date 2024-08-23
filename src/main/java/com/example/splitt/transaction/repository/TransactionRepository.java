package com.example.splitt.transaction.repository;

import com.example.splitt.transaction.model.transaction.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select distinct t from Transaction t " +
        "join Entry e on t.id = e.transaction.id " +
        "where e.group.id = :groupId")
    List<Transaction> findAllByGroupId(@Param("groupId") Long groupId, Pageable pageable);

}
