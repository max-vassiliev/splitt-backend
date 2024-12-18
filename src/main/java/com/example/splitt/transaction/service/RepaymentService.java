package com.example.splitt.transaction.service;

import com.example.splitt.transaction.dto.repayment.RepaymentCreateDto;
import com.example.splitt.transaction.dto.repayment.RepaymentOutBasicDto;
import com.example.splitt.transaction.dto.repayment.RepaymentOutDto;
import com.example.splitt.transaction.dto.transaction.GetTransactionParams;

public interface RepaymentService {

    RepaymentOutDto add(RepaymentCreateDto dto);

    RepaymentOutBasicDto getById(GetTransactionParams params);

}
