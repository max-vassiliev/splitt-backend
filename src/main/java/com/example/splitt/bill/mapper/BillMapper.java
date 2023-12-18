package com.example.splitt.bill.mapper;

import com.example.splitt.bill.dto.expense.ExpenseOutDto;
import com.example.splitt.bill.dto.expense.ExpenseCreateDto;
import com.example.splitt.bill.dto.repayment.RepaymentCreateDto;
import com.example.splitt.bill.dto.repayment.RepaymentOutDto;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.dto.UserSplitOutDto;
import com.example.splitt.bill.model.bill.Bill;
import com.example.splitt.bill.model.bill.BillType;
import com.example.splitt.user.dto.UserOutShortDto;
import com.example.splitt.user.mapper.UserMapper;
import com.example.splitt.user.model.User;
import com.example.splitt.util.SplittValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillMapper {

    private static final String REPAYMENT_DEFAULT_TITLE = "Repayment ##autotitle##";

    private final BillMapperLite billMapperLite;

    private final TransactionMapperLite transactionMapperLite;

    private final UserMapper userMapper;

    private final SplittValidator splittValidator;

    public Bill toExpenseBill(ExpenseCreateDto dto, User requester) {
        Bill bill = billMapperLite.toExpenseBill(dto);
        bill.setType(BillType.EXPENSE);
        if (splittValidator.isEmpty(dto.getNote())) {
            dto.setNote(null);
        }
        bill.setAddedBy(requester);
        bill.setAddedOn(LocalDateTime.now());
        return bill;
    }

    public Bill toRepaymentBill(RepaymentCreateDto dto, User requester) {
        Bill bill = billMapperLite.toRepaymentBill(dto);
        bill.setType(BillType.REPAYMENT);
        if (dto.getTitle() == null || splittValidator.isEmpty(dto.getTitle())) {
            bill.setTitle(REPAYMENT_DEFAULT_TITLE);
        }
        if (splittValidator.isEmpty(dto.getNote())) {
            bill.setNote(null);
        }
        bill.setAddedBy(requester);
        bill.setAddedOn(LocalDateTime.now());
        return bill;
    }

    public ExpenseOutDto toExpenseOutDto(Bill bill, List<UserBalanceOutDto> groupBalances) {
        UserOutShortDto addedBy = userMapper.toUserOutShortDto(bill.getAddedBy());
        List<UserSplitOutDto> paidBy = bill.getPayments().stream()
                .map(transactionMapperLite::toUserSplitOutDto)
                .collect(Collectors.toList());
        List<UserSplitOutDto> debtShares = bill.getDebts().stream()
                .map(transactionMapperLite::toUserSplitOutDto)
                .collect(Collectors.toList());

        ExpenseOutDto outputDto = billMapperLite.toExpenseOutDto(bill);
        outputDto.setAddedBy(addedBy);
        outputDto.setPaidBy(paidBy);
        outputDto.setDebtShares(debtShares);
        outputDto.setGroupBalances(groupBalances);

        return outputDto;
    }

    public RepaymentOutDto toRepaymentOutDto(Bill bill, List<UserBalanceOutDto> groupBalances) {
        UserOutShortDto payer = userMapper.toUserOutShortDto(bill.getRepaymentFrom().getUser());
        UserOutShortDto recipient = userMapper.toUserOutShortDto(bill.getRepaymentTo().getUser());
        UserOutShortDto addedBy = userMapper.toUserOutShortDto(bill.getAddedBy());

        RepaymentOutDto outputDto = billMapperLite.toRepaymentOutDto(bill);
        outputDto.setPayer(payer);
        outputDto.setRecipient(recipient);
        outputDto.setAddedBy(addedBy);
        outputDto.setGroupBalances(groupBalances);

        return outputDto;
    }
}
