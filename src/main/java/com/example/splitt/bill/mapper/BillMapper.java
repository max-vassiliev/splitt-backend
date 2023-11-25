package com.example.splitt.bill.mapper;

import com.example.splitt.bill.dto.ExpenseBalanceOutDto;
import com.example.splitt.bill.dto.ExpenseCreateDto;
import com.example.splitt.bill.model.Bill;
import com.example.splitt.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillMapper {

    private final BillMapperLite billMapperLite;

    public Bill toExpenseBill(ExpenseCreateDto dto, User requester) {
        Bill bill = billMapperLite.toExpenseBill(dto);
        bill.setAddedBy(requester);
        bill.setAddedOn(LocalDateTime.now());
        return bill;
    }

    public ExpenseBalanceOutDto toExpenseBalanceOutDto(Bill bill) {
        return null;
    }
}
