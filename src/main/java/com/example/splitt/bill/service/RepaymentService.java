package com.example.splitt.bill.service;

import com.example.splitt.bill.dto.repayment.RepaymentCreateDto;
import com.example.splitt.bill.dto.repayment.RepaymentOutDto;

public interface RepaymentService {

    RepaymentOutDto add(RepaymentCreateDto dto);

}
