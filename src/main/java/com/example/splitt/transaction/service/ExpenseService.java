package com.example.splitt.transaction.service;

import com.example.splitt.transaction.dto.expense.ExpenseOutDto;
import com.example.splitt.transaction.dto.expense.ExpenseCreateDto;

public interface ExpenseService {
    ExpenseOutDto add(ExpenseCreateDto expenseDto);

}
