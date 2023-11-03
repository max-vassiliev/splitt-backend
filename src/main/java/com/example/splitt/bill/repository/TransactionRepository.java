package com.example.splitt.bill.repository;

import com.example.splitt.bill.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
