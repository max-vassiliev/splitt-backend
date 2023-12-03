package com.example.splitt.bill.service;

import com.example.splitt.bill.dto.expense.ExpenseOutDto;
import com.example.splitt.bill.dto.expense.ExpenseCreateDto;

public interface ExpenseService {
    ExpenseOutDto add(ExpenseCreateDto expenseDto);

}
