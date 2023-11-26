package com.example.splitt.bill.mapper;

import com.example.splitt.bill.dto.ExpenseBalanceOutDto;
import com.example.splitt.bill.dto.ExpenseCreateDto;
import com.example.splitt.bill.model.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BillMapperLite {

    String SPLITT_DATE_FORMAT = "yyyy-MM-dd";

    String SPLITT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Mapping(target = "type", expression = "java(BillType.EXPENSE)")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "amountToInt")
    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    Bill toExpenseBill(ExpenseCreateDto expenseDto);

    @Mapping(target = "addedBy", ignore = true)
    @Mapping(target = "amount", source = "amount", qualifiedByName = "amountToInt")
    @Mapping(target = "date", source = "date", dateFormat = SPLITT_DATE_FORMAT)
    @Mapping(target = "addedOn", source = "addedOn", dateFormat = SPLITT_DATE_TIME_FORMAT)
    ExpenseBalanceOutDto toExpenseBalanceOutDto(Bill bill);

    @Named("amountToInt")
    static int mapAmountToInt(Float amount) {
        return (int) (amount * 100);
    }

    @Named("amountToInt")
    static float mapAmountToFloat(int amount) {
        return (float) (amount / 100);
    }

}
