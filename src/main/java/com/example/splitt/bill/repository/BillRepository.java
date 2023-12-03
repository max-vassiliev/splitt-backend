package com.example.splitt.bill.repository;

import com.example.splitt.bill.model.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
