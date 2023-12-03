package com.example.splitt.bill.repository;

import com.example.splitt.bill.model.bill.BillPayer;
import com.example.splitt.bill.model.bill.BillPayerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillPaymentRepository extends JpaRepository<BillPayer, BillPayerId> {
}
