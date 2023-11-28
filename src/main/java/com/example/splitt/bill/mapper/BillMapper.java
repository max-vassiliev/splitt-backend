package com.example.splitt.bill.mapper;

import com.example.splitt.bill.dto.ExpenseBalanceOutDto;
import com.example.splitt.bill.dto.ExpenseCreateDto;
import com.example.splitt.bill.dto.UserSplitOutDto;
import com.example.splitt.bill.model.Bill;
import com.example.splitt.bill.model.BillType;
import com.example.splitt.user.dto.UserOutShortDto;
import com.example.splitt.user.mapper.UserMapper;
import com.example.splitt.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillMapper {

    private final BillMapperLite billMapperLite;

    private final TransactionMapperLite transactionMapperLite;

    private final UserMapper userMapper;

    public Bill toExpenseBill(ExpenseCreateDto dto, User requester) {
        Bill bill = billMapperLite.toExpenseBill(dto);
        bill.setType(BillType.EXPENSE);
        bill.setAddedBy(requester);
        bill.setAddedOn(LocalDateTime.now());
        return bill;
    }

    public ExpenseBalanceOutDto toExpenseBalanceOutDto(Bill bill) {
        UserOutShortDto addedBy = userMapper.toUserOutShortDto(bill.getAddedBy());
        List<UserSplitOutDto> paidBy = bill.getPayments().stream()
                .map(transactionMapperLite::toUserSplitOutDto)
                .collect(Collectors.toList());
        List<UserSplitOutDto> debtShares = bill.getDebts().stream()
                .map(transactionMapperLite::toUserSplitOutDto)
                .collect(Collectors.toList());

        // TODO List<GroupBalanceOutDto> groupBalance;

        ExpenseBalanceOutDto outputDto = billMapperLite.toExpenseBalanceOutDto(bill);
        outputDto.setAddedBy(addedBy);
        outputDto.setPaidBy(paidBy);
        outputDto.setDebtShares(debtShares);

        return outputDto;
    }
}
