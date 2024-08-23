package com.example.splitt.transaction.service;

import com.example.splitt.transaction.dto.repayment.RepaymentCreateDto;
import com.example.splitt.transaction.dto.repayment.RepaymentOutDto;

public interface RepaymentService {

    RepaymentOutDto add(RepaymentCreateDto dto);

}
