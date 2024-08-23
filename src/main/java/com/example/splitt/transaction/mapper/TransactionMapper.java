package com.example.splitt.transaction.mapper;

import com.example.splitt.transaction.dto.expense.ExpenseOutDto;
import com.example.splitt.transaction.dto.expense.ExpenseCreateDto;
import com.example.splitt.transaction.dto.repayment.RepaymentCreateDto;
import com.example.splitt.transaction.dto.repayment.RepaymentOutDto;
import com.example.splitt.transaction.dto.transaction.TransactionOutShortDto;
import com.example.splitt.transaction.model.transaction.SplittType;
import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.util.balance.dto.UserBalanceOutDto;
import com.example.splitt.util.balance.dto.UserSplittOutDto;
import com.example.splitt.transaction.model.transaction.TransactionType;
import com.example.splitt.util.SplittValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionMapper {

    private static final String REPAYMENT_DEFAULT_TITLE = "Repayment";

    private final TransactionMapperLite transactionMapperLite;

    private final EntryMapperLite entryMapperLite;

    private final SplittValidator splittValidator;

    public Transaction toExpenseTransaction(ExpenseCreateDto dto, Long requesterId) {
        Transaction transaction = transactionMapperLite.toExpenseTransaction(dto);
        SplittType splittType = SplittType.fromString(dto.getSplittType());

        transaction.setType(TransactionType.EXPENSE);
        transaction.setSplittType(splittType);
        if (splittValidator.isEmpty(dto.getNote())) {
            dto.setNote(null);
        }
        transaction.setAddedByUserId(requesterId);
        transaction.setAddedOn(LocalDateTime.now());

        return transaction;
    }

    public Transaction toRepaymentTransaction(RepaymentCreateDto dto, Long requesterId) {
        Transaction transaction = transactionMapperLite.toRepaymentTransaction(dto);

        transaction.setType(TransactionType.REPAYMENT);
        transaction.setTitle(REPAYMENT_DEFAULT_TITLE);
        transaction.setSplittType(SplittType.NONE);
        if (splittValidator.isEmpty(dto.getNote())) {
            transaction.setNote(null);
        }
        transaction.setAddedByUserId(requesterId);
        transaction.setAddedOn(LocalDateTime.now());

        return transaction;
    }

    public ExpenseOutDto toExpenseOutDto(Transaction transaction, List<UserBalanceOutDto> groupBalances) {
        Long addedByUserId = transaction.getAddedByUserId();
        List<UserSplittOutDto> paidBy = transaction.getPayments().stream()
                .map(entryMapperLite::toUserSplittOutDto)
                .toList();
        List<UserSplittOutDto> splitts = transaction.getSplitts().stream()
                .map(entryMapperLite::toUserSplittOutDto)
                .toList();

        ExpenseOutDto outputDto = transactionMapperLite.toExpenseOutDto(transaction);
        outputDto.setAddedByUserId(addedByUserId);
        outputDto.setPaidBy(paidBy);
        outputDto.setSplitts(splitts);
        outputDto.setGroupBalances(groupBalances);

        return outputDto;
    }

    public RepaymentOutDto toRepaymentOutDto(Transaction repayment, List<UserBalanceOutDto> groupBalances) {
        Long payerId = repayment.getRepaymentFrom().getUserId();
        Long recipientId = repayment.getRepaymentTo().getUserId();
        Long addedByUserId = repayment.getAddedByUserId();

        RepaymentOutDto outputDto = transactionMapperLite.toRepaymentOutDto(repayment);
        outputDto.setPayerId(payerId);
        outputDto.setRecipientId(recipientId);
        outputDto.setAddedByUserId(addedByUserId);
        outputDto.setGroupBalances(groupBalances);

        return outputDto;
    }

    public TransactionOutShortDto toTransactionOutShortDto(Transaction transaction) {
        return TransactionType.REPAYMENT.equals(transaction.getType())
                ? transactionMapperLite.toRepaymentOutShortDto(transaction)
                : transactionMapperLite.toExpenseOutShortDto(transaction);
    }
}
