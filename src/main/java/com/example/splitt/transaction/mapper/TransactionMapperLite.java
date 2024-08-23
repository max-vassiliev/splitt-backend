package com.example.splitt.transaction.mapper;

import com.example.splitt.transaction.dto.expense.ExpenseOutDto;
import com.example.splitt.transaction.dto.expense.ExpenseCreateDto;
import com.example.splitt.transaction.dto.expense.ExpenseOutShortDto;
import com.example.splitt.transaction.dto.repayment.RepaymentCreateDto;
import com.example.splitt.transaction.dto.repayment.RepaymentOutDto;
import com.example.splitt.transaction.dto.repayment.RepaymentOutShortDto;
import com.example.splitt.transaction.model.transaction.SplittType;
import com.example.splitt.transaction.model.transaction.Transaction;
import com.example.splitt.transaction.model.transaction.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TransactionMapperLite {

    String SPLITT_DATE_FORMAT = "yyyy-MM-dd";

    String SPLITT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "splittType", ignore = true)
    Transaction toExpenseTransaction(ExpenseCreateDto expenseDto);

    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "splittType", ignore = true)
    Transaction toRepaymentTransaction(RepaymentCreateDto dto);

    @Mapping(target = "addedByUserId", ignore = true)
    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "addedOn", source = "addedOn", dateFormat = SPLITT_DATE_TIME_FORMAT)
    @Mapping(target = "splittType", source = "splittType", qualifiedByName = "mapSplittTypeToString")
    ExpenseOutDto toExpenseOutDto(Transaction expense);

    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "type", source = "type", qualifiedByName = "mapTransactionTypeToString")
    ExpenseOutShortDto toExpenseOutShortDto(Transaction expense);

    @Mapping(target = "addedByUserId", ignore = true)
    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "addedOn", source = "addedOn", dateFormat = SPLITT_DATE_TIME_FORMAT)
    RepaymentOutDto toRepaymentOutDto(Transaction repayment);

    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "type", source = "type", qualifiedByName = "mapTransactionTypeToString")
    @Mapping(target = "payerId", source = "repayment.repaymentFrom.userId")
    @Mapping(target = "recipientId", source = "repayment.repaymentTo.userId")
    RepaymentOutShortDto toRepaymentOutShortDto(Transaction repayment);

    @Named("mapSplittTypeToString")
    default String mapSplittTypeToString(SplittType splittType) {
        return splittType != null ? splittType.name().toLowerCase() : "none";
    }

    @Named("mapTransactionTypeToString")
    default String mapTransactionTypeToString(TransactionType transactionType) {
        return transactionType != null ? transactionType.name().toLowerCase() : "none";
    }
}
