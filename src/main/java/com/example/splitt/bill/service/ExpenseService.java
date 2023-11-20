package com.example.splitt.bill.service;

import com.example.splitt.bill.dto.ExpenseBalanceOutDto;
import com.example.splitt.bill.dto.ExpenseCreateDto;

public interface ExpenseService {
    ExpenseBalanceOutDto add(ExpenseCreateDto expenseDto);

}
