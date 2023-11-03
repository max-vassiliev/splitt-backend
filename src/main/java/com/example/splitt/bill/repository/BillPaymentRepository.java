package com.example.splitt.bill.repository;

import com.example.splitt.bill.model.BillPayment;
import com.example.splitt.bill.model.BillPaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillPaymentRepository extends JpaRepository<BillPayment, BillPaymentId> {
}
